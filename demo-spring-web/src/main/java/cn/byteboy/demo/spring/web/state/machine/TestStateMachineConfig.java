package cn.byteboy.demo.spring.web.state.machine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;


/**
 * @author hongshaochuan
 */
//@Configuration
@EnableStateMachine(name = "testStateMachine")
public class TestStateMachineConfig extends StateMachineConfigurerAdapter<StateEnum, EventEnum> {

    @Override
    public void configure(StateMachineStateConfigurer<StateEnum, EventEnum> states) throws Exception {
        states.withStates()
                .initial(StateEnum.INIT)
                .states(EnumSet.allOf(StateEnum.class));
    }

//    @Override
//    public void configure(StateMachineConfigurationConfigurer<StateEnum, EventEnum> config) throws Exception {
//        config.withConfiguration().listener();
//    }

    @Override
    public void configure(StateMachineTransitionConfigurer<StateEnum, EventEnum> transitions) throws Exception {
        transitions
                .withExternal().source(StateEnum.INIT).target(StateEnum.S1).event(EventEnum.E1)
                .and()
                .withExternal().source(StateEnum.INIT).target(StateEnum.S2).event(EventEnum.E2)
                .and()
                .withExternal().source(StateEnum.INIT).target(StateEnum.S2).event(EventEnum.E3);
    }

    @Bean
    public DefaultStateMachinePersister<StateEnum, EventEnum, TestPOJO> persister() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<StateEnum, EventEnum, TestPOJO>() {
            @Override
            public void write(StateMachineContext<StateEnum, EventEnum> context, TestPOJO contextObj) throws Exception {
                System.out.println("写入状态");
            }

            @Override
            public StateMachineContext<StateEnum, EventEnum> read(TestPOJO pojo) throws Exception {
                System.out.println("读取状态");
                return new DefaultStateMachineContext<>(pojo.getState(), null, null, null);
            }
        });
    }
}
