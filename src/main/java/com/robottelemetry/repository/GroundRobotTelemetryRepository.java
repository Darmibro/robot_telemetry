package com.robottelemetry.repository;

import com.robottelemetry.repository.entity.GroundRobotTelemetryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroundRobotTelemetryRepository extends JpaRepository<GroundRobotTelemetryEntity,Long> {

    Optional<GroundRobotTelemetryEntity> findByRobotId(String robotId);
}
