package com.example.spring_scheduler.noti;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution // 동시 실행 방지
public class OyoOrderNotificationJob implements Job {

	// *Service service;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("execute");
		System.out.println("올리브영 가고싶다아!!!!!!!!! = " + "갈 수 있을까?");
	}
}