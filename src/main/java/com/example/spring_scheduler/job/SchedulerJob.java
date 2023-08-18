package com.example.spring_scheduler.job;

import com.example.spring_scheduler.config.SchedulerConfig;
import com.example.spring_scheduler.constant.BatchConstant;
import com.example.spring_scheduler.noti.OyoOrderNotificationJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class SchedulerJob {

    // watchJob Job 등록
    @Bean(name = "watchJob")
    public JobDetailFactoryBean watchJob() {
        return SchedulerConfig.createJobDetail(WatchJob.class);
    }

    // watchJob Job SimpleTrigger 등록
    @Bean(name = "watchJobTrigger")
    public SimpleTriggerFactoryBean watchJobTrigger(@Qualifier("watchJob") JobDetail jobDetail) {
        return SchedulerConfig.createSimpleTrigger(jobDetail, BatchConstant.SECOND_5MINUTE);
    }

    // 올리브영 주문채널 알림 Job 등록
    @Bean(name = "oyoOrderNotiJob")
    public JobDetailFactoryBean oyoOrderNotificationJob() {
        return SchedulerConfig.createJobDetail(OyoOrderNotificationJob.class);
    }

    // 올리브영 주문채널 알림 Job SimpleTrigger 등록
    @Bean(name = "oyoOrderNotificationJobTrigger")
    public SimpleTriggerFactoryBean oyoOrderNotificationJobTrigger(@Qualifier("oyoOrderNotiJob") JobDetail jobDetail) {
        return SchedulerConfig.createSimpleTrigger(jobDetail, BatchConstant.SECOND_10SEC);
    }
}
