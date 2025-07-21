package com.robottelemetry.repository;

import com.robottelemetry.repository.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    List<AlertEntity> findAllByStatus(String active);
}
