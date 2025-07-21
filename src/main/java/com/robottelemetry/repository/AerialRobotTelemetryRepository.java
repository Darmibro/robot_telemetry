package com.robottelemetry.repository;

import com.robottelemetry.repository.entity.AerialRobotTelemetryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AerialRobotTelemetryRepository extends JpaRepository<AerialRobotTelemetryEntity, Long> {
    Optional<AerialRobotTelemetryEntity> findByRobotId(String robotId);
}
