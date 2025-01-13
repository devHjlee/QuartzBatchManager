package com.quartzbatchmanager.quartz.application;

import com.quartzbatchmanager.quartz.dto.SchedulerJobDTO;
import com.quartzbatchmanager.quartz.entity.SchedulerErrorLog;
import com.quartzbatchmanager.quartz.entity.SchedulerHistory;
import com.quartzbatchmanager.quartz.repository.SchedulerErrorLogRepository;
import com.quartzbatchmanager.quartz.repository.SchedulerHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SchedulerLogService {

    private final SchedulerErrorLogRepository schedulerErrorLogRepository;
    private final SchedulerHistoryRepository schedulerHistoryRepository;

    public void historySave(SchedulerJobDTO schedulerJobDTO) {
        schedulerHistoryRepository.save(SchedulerHistory
                .builder()
                .schedName(schedulerJobDTO.getSchedName())
                .jobName(schedulerJobDTO.getJobName())
                .jobGroup(schedulerJobDTO.getJobGroup())
                .jobClass(schedulerJobDTO.getJobClass())
                .triggerName(schedulerJobDTO.getJobName().concat("Trigger"))
                .triggerGroup(schedulerJobDTO.getJobGroup())
                .scheduleState(schedulerJobDTO.getScheduleStat())
                .cronExpression(schedulerJobDTO.getCronExpression())
                .reason(schedulerJobDTO.getReason())
                .build());
    }

    public void errorLogSave(SchedulerErrorLog schedulerErrorLog) {
        schedulerErrorLogRepository.save(schedulerErrorLog);
    }
}
