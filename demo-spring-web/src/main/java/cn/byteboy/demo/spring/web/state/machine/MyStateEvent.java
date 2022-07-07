package cn.byteboy.demo.spring.web.state.machine;

import org.springframework.context.ApplicationEvent;
import org.springframework.messaging.Message;

/**
 * @author hongshaochuan
 */
public class MyStateEvent extends ApplicationEvent {

    public MyStateEvent(Message<EventEnum> source) {
        super(source);
    }
}
