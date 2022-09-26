package cn.byteboy.demo.spring.gateway.schedule;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author hongshaochuan
 */
@Component
@EnableScheduling
public class ScheduleDemo {

    @ScheduledDistributed
    @Scheduled(cron = "* * * * * ?")
    public void schedule() {
        System.out.println(new Date());
    }
}
