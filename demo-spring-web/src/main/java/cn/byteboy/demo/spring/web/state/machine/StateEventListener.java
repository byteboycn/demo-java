package cn.byteboy.demo.spring.web.state.machine;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author hongshaochuan
 */
@Component
public class StateEventListener implements ApplicationListener<MyStateEvent> {


    @Override
    public void onApplicationEvent(MyStateEvent event) {
        System.out.println("listener1:" +  event.getSource());
    }
}
