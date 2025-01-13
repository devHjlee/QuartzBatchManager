package com.quartzbatchmanager.quartz.application;

import com.quartzbatchmanager.quartz.dto.SchedulerJobDTO;
import com.quartzbatchmanager.quartz.util.QuartzUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final SchedulerLogService schedulerLogService;

    private final ApplicationContext context;

    public void addScheduleJob(SchedulerJobDTO schedulerJobDTO) {
        JobDetail jobDetail;
        Trigger trigger;
        Class<Job> jobClass = null;

        try {
            jobClass = (Class<Job>) Class.forName("com.quartzbatchmanager.quartz.job." +schedulerJobDTO.getJobClass());
            jobDetail = QuartzUtils.createJob(schedulerJobDTO,jobClass,context);
            trigger = QuartzUtils.createTrigger(schedulerJobDTO);

            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail,trigger);

            schedulerJobDTO.setSchedName(schedulerFactoryBean.getScheduler().getSchedulerName());
            schedulerLogService.historySave(schedulerJobDTO);
        } catch (SchedulerException | ClassNotFoundException e) {
            log.error(e.getMessage());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public boolean updateScheduleJob(SchedulerJobDTO schedulerJobDTO) {
        JobKey jobKey = null;
        Trigger newTrigger;

        try {
            newTrigger = QuartzUtils.createTrigger(schedulerJobDTO);
            jobKey = JobKey.jobKey(schedulerJobDTO.getJobName(), schedulerJobDTO.getJobGroup());

            Date dt = schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(schedulerJobDTO.getJobName().concat("Trigger"),schedulerJobDTO.getJobGroup()), newTrigger);
            log.debug("Job with jobKey : {} rescheduled successfully at date : {}", jobKey, dt);
            return true;
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job with jobKey : {}", schedulerJobDTO.getJobName(), e);
        } catch (IllegalArgumentException e){
            log.error("[schedulerdebug] error occurred while checking job with jobKey : {}", schedulerJobDTO.getJobName(), e);
            throw new IllegalArgumentException(e);
        }
        return false;
    }

    public boolean deleteScheduleJob(SchedulerJobDTO schedulerJobDTO) {
        JobKey jobKey = JobKey.jobKey(schedulerJobDTO.getJobName(), schedulerJobDTO.getJobGroup());
        try {
            schedulerFactoryBean.getScheduler().deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean pauseScheduleJob(SchedulerJobDTO schedulerJobDTO) {
        JobKey jobKey = JobKey.jobKey(schedulerJobDTO.getJobName(), schedulerJobDTO.getJobGroup());
        try {
            schedulerFactoryBean.getScheduler().pauseJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean resumeScheduleJob(SchedulerJobDTO schedulerJobDTO) {
        JobKey jobKey = JobKey.jobKey(schedulerJobDTO.getJobName(), schedulerJobDTO.getJobGroup());
        try {
            schedulerFactoryBean.getScheduler().resumeJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean immediatelyJob(SchedulerJobDTO schedulerJobDTO){
        JobKey jobKey = JobKey.jobKey(schedulerJobDTO.getJobName(), schedulerJobDTO.getJobGroup());
        JobDataMap jobDataMap = new JobDataMap(schedulerJobDTO.getJobDataMap());

        try {
            schedulerFactoryBean.getScheduler().triggerJob(jobKey,jobDataMap);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean isJobRunning(SchedulerJobDTO schedulerJobDTO) {
        try {
            List<JobExecutionContext> currentJobs = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
            if (currentJobs != null) {
                for (JobExecutionContext jobCtx : currentJobs) {
                    if (schedulerJobDTO.getJobName().equals(jobCtx.getJobDetail().getKey().getName())) {
                        return true;
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean isJobExists(SchedulerJobDTO schedulerJobDTO) {
        JobKey jobKey = new JobKey(schedulerJobDTO.getJobName(),schedulerJobDTO.getJobGroup());
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jobKey)) {
                return true;
            }

        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public String getScheduleState(SchedulerJobDTO schedulerJobDTO) {
        JobKey jobKey = JobKey.jobKey(schedulerJobDTO.getJobName(), schedulerJobDTO.getJobGroup());
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());

            if (triggers != null && triggers.size() > 0) {
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    if (Trigger.TriggerState.NORMAL.equals(triggerState)) {
                        return "SCHEDULED";
                    }
                    return triggerState.name().toUpperCase();
                }
            }
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
