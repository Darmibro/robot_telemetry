package com.robottelemetry.adapter;

import com.robottelemetry.excaption.RobotNotFoundException;
import com.robottelemetry.repository.*;
import com.robottelemetry.repository.entity.AerialRobotTelemetryEntity;
import com.robottelemetry.repository.entity.AlertEntity;
import com.robottelemetry.repository.entity.GroundRobotTelemetryEntity;
import com.robottelemetry.repository.entity.RobotStatusEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RobotTelemetryAdapter {
    private final GroundRobotTelemetryRepository groundRobotTelemetryRepository;
    private final AerialRobotTelemetryRepository aerialRobotTelemetryRepository;
    private final AlertRepository alertRepository;
    private final RobotStatusRepository robotStatusRepository;

    public void saveAerialRobotTelemetry(AerialRobotTelemetryEntity entity){
        aerialRobotTelemetryRepository.save(entity);
    }

    public void saveGroundRobotTelemetry(GroundRobotTelemetryEntity entity){
        groundRobotTelemetryRepository.save(entity);
    }

    public void saveAlert(AlertEntity alertEntity){
        alertRepository.save(alertEntity);
    }

    public void updateRobotStatus(RobotStatusEntity robotStatusEntity){
        robotStatusRepository.save(robotStatusEntity);
    }

    public List<RobotStatusEntity> getAllRobotsByStatus(String status){
        return robotStatusRepository.findAllByStatus(status);
    }

    public RobotStatusEntity getRobotById(String robotId){
        return robotStatusRepository.findByRobotId(robotId)
                .orElseThrow(() -> new RobotNotFoundException(robotId));
    }

    public GroundRobotTelemetryEntity getTelemetryForGroundRobot(String robotId){
        return groundRobotTelemetryRepository.findByRobotId(robotId)
                .orElseThrow(() -> new RobotNotFoundException(robotId));
    }

    public AerialRobotTelemetryEntity getTelemetryForAerialRobot(String robotId){
        return aerialRobotTelemetryRepository.findByRobotId(robotId)
                .orElseThrow(() -> new RobotNotFoundException(robotId));
    }

    public List<AlertEntity> getActiveAlerts(){
       return alertRepository.findAllByStatus("active");
    }
}
