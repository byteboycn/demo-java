package cn.byteboy.demo.spring.web.state.machine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author hongshaochuan
 */
@Service
public class BusinessService {

//    @Autowired
    private StateMachine<StateEnum, EventEnum> testStateMachine;

//    @Autowired
    private StateMachinePersister<StateEnum, EventEnum, TestPOJO> persister;

    @Autowired
    @Lazy
    private BusinessService self;

    public void b1() {

        TestPOJO pojo = new TestPOJO();
        pojo.setState(StateEnum.INIT);

        Message<EventEnum> message = MessageBuilder.withPayload(EventEnum.E3).setHeader("pojo", null).build();

        try {

            StateMachine<StateEnum, EventEnum> restore = persister.restore(testStateMachine, pojo);
            restore.startReactively().block();
//            restore.start();
//            boolean res = testStateMachine.sendEvent(message);
//            System.out.println("res: " + res);
            Flux<StateMachineEventResult<StateEnum, EventEnum>> stateMachineEventResultFlux = restore.sendEvent(Mono.just(message));
            stateMachineEventResultFlux.subscribe();
//            stateMachineEventResultFlux.doOnComplete(() -> {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("11");
//            }).subscribe();
            persister.persist(testStateMachine, pojo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            restore.stop();
        }
        System.out.println("111");
    }

    public void b0() {
        System.out.println("b0");
        self.b2();
    }

    @Transactional(rollbackFor = Exception.class)
    public void b2() {
        System.out.println("b2");
        self.middle();
    }

    public void middle() {
        try {
            self.b3();
        } catch (Exception e) {
            System.out.println("ignore exception");
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void b3() {
        System.out.println("b3");
        System.out.println(1/0);
    }
}
