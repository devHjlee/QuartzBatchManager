package com.quartzbatchmanager.batch.repository;

import com.quartzbatchmanager.batch.entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<BatchEntity,Long> {
}
