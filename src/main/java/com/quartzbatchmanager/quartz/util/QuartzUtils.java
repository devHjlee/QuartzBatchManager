package com.quartzbatchmanager.quartz.util;

import com.quartzbatchmanager.quartz.dto.SchedulerJobDTO;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;

import static org.quartz.CronExpression.isValidExpression;

public class QuartzUtils {

    private QuartzUtils() {
    }

    /**
     * Job 생성
     * @param quartzJobDTO - Quartz Job 정보
     * @param jobClass - Job 생성할 Class
     * @param context - ApplicationContext
     * @return JobDetail
     */
    public static JobDetail createJob(SchedulerJobDTO quartzJobDTO, Class<? extends Job> jobClass, ApplicationContext context) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(false);
        factoryBean.setApplicationContext(context);
        factoryBean.setName(quartzJobDTO.getJobName());
        factoryBean.setGroup(quartzJobDTO.getJobGroup());
        factoryBean.setDescription(quartzJobDTO.getDesc());
        if (quartzJobDTO.getJobDataMap() != null) {
            factoryBean.setJobDataMap(quartzJobDTO.getJobDataMap());
        }

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();

//        return JobBuilder
//                .newJob(jobClass)
//                .withIdentity(jobRequest.getJobName(),jobRequest.getJobGroup())
//                .storeDurably(false)
//                .withDescription(jobRequest.getDesc())
//                .usingJobData(jobRequest.getJobDataMap())
//                .build();
    }

    /**
     * Trigger 생성(Cron,Simple)
     * @param quartzJobDTO - Quartz Job 정보
     * @return Trigger
     */
    public static Trigger createTrigger(SchedulerJobDTO quartzJobDTO) {
        String cronExpression = quartzJobDTO.getCronExpression();
        if (!isValidExpression(cronExpression)) {
            throw new IllegalArgumentException("Provided expression " + cronExpression + " is not a valid cron expression");
        } else {
            return createCronTrigger(quartzJobDTO);
        }
    }

    /**
     * CronTrigger 생성
     * @param quartzJobDTO - Quartz Job 정보
     * @return Trigger
     */
    private static Trigger createCronTrigger(SchedulerJobDTO quartzJobDTO) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("failCnt","0");
        jobDataMap.put("stop","N");
        jobDataMap.put("retry",quartzJobDTO.getRetry());
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(quartzJobDTO.getJobName().concat("Trigger"));
        factoryBean.setGroup(quartzJobDTO.getJobGroup());
        factoryBean.setCronExpression(quartzJobDTO.getCronExpression());
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        factoryBean.setJobDataMap(jobDataMap);
        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return factoryBean.getObject();
    }

    /**
     * SimpleTrigger 생성
     * @param quartzJobDTO - Quartz Job 정보
     * @return Trigger
     */
    private static Trigger createSimpleTrigger(SchedulerJobDTO quartzJobDTO) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(quartzJobDTO.getJobName());
        factoryBean.setGroup(quartzJobDTO.getJobGroup());
        factoryBean.setStartTime(Date.from(quartzJobDTO.getStartDateAt().atZone(ZoneId.systemDefault()).toInstant()));
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        factoryBean.setRepeatInterval(quartzJobDTO.getRepeatIntervalInSeconds() * 1000); //ms 단위임
        factoryBean.setRepeatCount(quartzJobDTO.getRepeatCount());

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
