package com.quartzbatchmanager.quartz.dto;

import lombok.Getter;
import lombok.Setter;
import org.quartz.JobDataMap;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class SchedulerJobDTO {

    private final String jobGroup = "DEV";

    private String schedName;
    private String retry = "N";
    private String jobName;
    private String jobClass;
    private String cronExpression;
    private String scheduleStat;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateAt;

    private long repeatIntervalInSeconds;
    private int repeatCount;
    private JobDataMap jobDataMap;
    private String desc;
    private String reason;

}
