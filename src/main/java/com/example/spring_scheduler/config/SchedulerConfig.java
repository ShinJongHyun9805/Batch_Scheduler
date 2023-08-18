package com.example.spring_scheduler.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private static final String SIMPLETRIGGER = "[Simple Trigger]";
    private static final String CRONTRIGGER = "[Cron Trigger]";

    private final ApplicationContext applicationContext;
    private final DataSource dataSource;

    // Quartz 스케줄러의 Job 인스턴스 생성. Spring DI 연결
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        log.info("QuartzConfig springBeanJobFactory");
        QuartzSpringBeanJobFactory jobFactory = new QuartzSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean createScheduler(Trigger... triggers) throws IOException {
        log.info("QuartzConfig createScheduler");
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setQuartzProperties(quartzProperties());
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        if (ArrayUtils.isNotEmpty(triggers)) {
            schedulerFactory.setTriggers(triggers);
        }
        return schedulerFactory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        log.info("QuartzConfig quartzProperties");
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/config/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static JobDetailFactoryBean createJobDetail(Class jobClass) {
        log.info("createJobDetail: " + jobClass.getName());

        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobClass.getSimpleName());
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        return factoryBean;

    }

    public static SimpleTriggerFactoryBean createSimpleTrigger(JobDetail job, long intervalSecond) {
        log.info("createSimpleTrigger: " + job.toString() + ", " + intervalSecond);

        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(job);
        factoryBean.setStartDelay(30L * 1000L);
        factoryBean.setRepeatInterval(intervalSecond * 1000L);
        factoryBean.setName(job.getJobClass().getSimpleName() + SIMPLETRIGGER + intervalSecond);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY); // forever
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);

        return factoryBean;
    }

    public static CronTriggerFactoryBean createCronTrigger(JobDetail job, String cronExpression) {
        log.info("createCronTrigger: " + job.toString() + ", " + cronExpression);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(job);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setStartTime(calendar.getTime());
        factoryBean.setStartDelay(30L * 1000L);
        factoryBean.setName(job.getJobClass().getSimpleName() + CRONTRIGGER + cronExpression);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);

        return factoryBean;
    }
}
