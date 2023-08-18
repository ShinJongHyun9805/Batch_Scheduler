package com.example.spring_scheduler.job;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class WatchJob implements Job {

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final ApplicationContext applicationContext;

    private final static String JOBTYPE = "org.quartz.impl.JobDetailImpl";

    // 스케줄링 된 작업이 실행 될 때 호출되는 메서드
    @Override 
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("execute");
        watchScheduler();
    }

    private void watchScheduler() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    log.info("JOB_INFO::{}:{}", jobName, jobGroup);
                    String expectBeanName = jobName.substring(0, 1).toLowerCase() + jobName.substring(1, jobName.length());
                    try {
                        if (!applicationContext.getBean(expectBeanName).getClass().getTypeName().equals(JOBTYPE)) {
                            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
                            if (!isJobRunning(jobName, jobGroup)) {
                                deleteJob(triggerKey, jobKey);
                            }
                        }
                    } catch (NoSuchBeanDefinitionException e) {
                        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
                        if (!isJobRunning(jobName, jobGroup)) {
                            deleteJob(triggerKey, jobKey);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("e", e);
        }

    }

    // 실행중인 JOB인지
    private boolean isJobRunning(String jobName, String jobGroup) throws SchedulerException {
        List<JobExecutionContext> currentJobs = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
        if (currentJobs != null) {
            for (JobExecutionContext jobCtx : currentJobs) {
                String jobNameDB = jobCtx.getJobDetail().getKey().getName();
                String groupNameDB = jobCtx.getJobDetail().getKey().getGroup();
                if (jobName.equalsIgnoreCase(jobNameDB) && jobGroup.equalsIgnoreCase(groupNameDB)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 중지된 JOB 삭제
    private void deleteJob(TriggerKey triggerKey, JobKey jobKey) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(jobKey);
    }
}
