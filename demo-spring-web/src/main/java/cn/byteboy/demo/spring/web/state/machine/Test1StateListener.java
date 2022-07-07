package cn.byteboy.demo.spring.web.state.machine;

import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author hongshaochuan
 */
//@Component
//@WithStateMachine(name = "testStateMachine")
public class Test1StateListener {

    @OnTransition(source = "INIT", target = "S1")
//    @OnTransition(source = "S1", target = "INIT")
    public boolean e1(Message<EventEnum> message) {
        System.out.println("e11-Listener");
        return true;
    }

    @OnTransition(source = "INIT", target = "S2")
    public boolean e2(Message<EventEnum> message) {
        System.out.println("e22-Listener");
        return true;
    }
}
