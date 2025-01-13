package com.quartzbatchmanager.quartz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Entity
@Table(name = "QUARTZ_ERROR_LOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SchedulerErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schedName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String result;
    private String exceptionMessage;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date startTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date endTime;

    @Builder
    public SchedulerErrorLog(String schedName,String triggerName,String triggerGroup,String jobName,String jobGroup,String result,String exceptionMessage,Date startTime,Date endTime){
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.result = result;
        this.exceptionMessage = exceptionMessage;
        this.startTime = startTime;
        this.endTime = endTime;

    }
}
