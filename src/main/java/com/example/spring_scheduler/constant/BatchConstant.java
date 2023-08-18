package com.example.spring_scheduler.constant;

public class BatchConstant {
	/* For SimpleTrigger */
	public static final long SECOND_1SEC = 1;
	public static final long SECOND_10SEC = SECOND_1SEC * 10;
	public static final long SECOND_20SEC = SECOND_1SEC * 20;
	public static final long SECOND_30SEC = SECOND_1SEC * 30;
	public static final long SECOND_1MINUTE = 60;
	public static final long SECOND_5MINUTE = SECOND_1MINUTE * 5L;
	public static final long SECOND_10MINUTE = SECOND_1MINUTE * 10L;
	public static final long SECOND_30MINUTE = SECOND_1MINUTE * 30L;
	public static final long SECOND_1HOUR = SECOND_1MINUTE * 60L;
	public static final long SECOND_12HOUR = SECOND_1HOUR * 12L;
	public static final long SECOND_24HOUR = SECOND_1HOUR * 24L;

	// For CronExpress http://www.cronmaker.com/?0

	// 테스트: 10분마다.
	// 0 0/10 * 1/1 * ? *

	// 테스트: 12분마다.
	// 0 0/12 * 1/1 * ? *

	// 매일 새벽 2시 정각에 실행
	public static final String CRON_EXPRESS_DAILY_BATCH = "0 0 2 1/1 * ? *";

	// 매달 1일 새벽 3시 정각에 실행
	public static final String CRON_EXPRESS_AT_MONTHLY_BATCH = "0 0 3 1 1/1 ? *";
	
}
