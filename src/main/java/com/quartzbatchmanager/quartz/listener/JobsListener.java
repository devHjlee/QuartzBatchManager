package com.quartzbatchmanager.quartz.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobsListener implements JobListener {


	@Override
	public String getName() {
		return "globalJob";
	}

	/**
	 * Job 수행 전
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobToBeExecuted :: jobKey : {}", jobKey);
	}

	/**
	 * Job 중단된 상태
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		JobKey jobKey = context.getJobDetail().getKey();
		log.info("jobExecutionVetoed :: jobKey : {}", jobKey);
	}

	/**
	 * Job 수행 완료 후
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

	}
}
