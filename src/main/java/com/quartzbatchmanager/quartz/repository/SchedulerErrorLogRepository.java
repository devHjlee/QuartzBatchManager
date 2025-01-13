package com.quartzbatchmanager.quartz.repository;

import com.quartzbatchmanager.quartz.entity.SchedulerErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerErrorLogRepository extends JpaRepository<SchedulerErrorLog, Long> {
}
