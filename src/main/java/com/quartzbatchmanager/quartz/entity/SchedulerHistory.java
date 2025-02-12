package com.quartzbatchmanager.quartz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "QUARTZ_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SchedulerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schedName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String jobClass;
    private String scheduleState;
    private String cronExpression;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private LocalDateTime createTime;
    private String reason;

    @Builder
    public SchedulerHistory(String schedName, String triggerName, String triggerGroup, String jobName, String jobGroup, String jobClass, String scheduleState, String cronExpression, LocalDateTime createTime, String reason){
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobClass = jobClass;
        this.scheduleState = scheduleState;
        this.cronExpression = cronExpression;
        this.createTime = createTime;
        this.reason = reason;

    }

}
