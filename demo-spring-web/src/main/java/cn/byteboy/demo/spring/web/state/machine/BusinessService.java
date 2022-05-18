package cn.byteboy.demo.spring.web.state.machine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

/**
 * @author hongshaochuan
 */
//@Service
public class BusinessService {

    @Autowired
    private StateMachine<StateEnum, EventEnum> testStateMachine;

    @Autowired
    private StateMachinePersister<StateEnum, EventEnum, TestPOJO> persister;

    public void b1() {

        TestPOJO pojo = new TestPOJO();
        pojo.setState(StateEnum.INIT);

        Message<EventEnum> message = MessageBuilder.withPayload(EventEnum.E1).setHeader("pojo", pojo).build();

        try {
            testStateMachine.start();
            persister.restore(testStateMachine, pojo);
            boolean res = testStateMachine.sendEvent(message);
            persister.persist(testStateMachine, pojo);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            testStateMachine.stop();
        }


    }
}
