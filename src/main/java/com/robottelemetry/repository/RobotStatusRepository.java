package com.robottelemetry.repository;

import com.robottelemetry.repository.entity.RobotStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RobotStatusRepository extends JpaRepository<RobotStatusEntity, Long> {

    List<RobotStatusEntity> findAllByStatus(String status);

    Optional<RobotStatusEntity> findByRobotId(String robotId);
}
