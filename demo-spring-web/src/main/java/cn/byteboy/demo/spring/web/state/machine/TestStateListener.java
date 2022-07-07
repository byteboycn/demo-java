package cn.byteboy.demo.spring.web.state.machine;

import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author hongshaochuan
 */
@Component
@WithStateMachine(name = "testStateMachine")
public class TestStateListener {

    @OnTransition(source = "INIT", target = "S1")
//    @OnTransition(source = "S1", target = "INIT")
    public boolean e1(Message<EventEnum> message) {
        System.out.println("e1-Listener");
        return true;
    }

    @OnTransition(source = "INIT", target = "S2")
    public boolean e2(Message<EventEnum> message) {
        System.out.println("e2-Listener");
        return true;
    }

    @OnTransition(source = "INIT", target = "S2")
    public boolean e3(Message<EventEnum> message) {
        System.out.println("e3-Listener");

        return true;
    }
}
