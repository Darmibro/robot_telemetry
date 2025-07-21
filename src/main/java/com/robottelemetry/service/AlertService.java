package com.robottelemetry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robottelemetry.adapter.RobotTelemetryAdapter;
import com.robottelemetry.adapter.RobotTelemetryMapper;
import com.robottelemetry.dto.Alert;
import com.robottelemetry.dto.AlertRobotTelemetry;
import com.robottelemetry.dto.RobotTelemetry;
import com.robottelemetry.repository.entity.AlertEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    private final RobotTelemetryStreamHandler streamHandler;
    private final RobotTelemetryAdapter robotTelemetryAdapter;

    public void sendAndSaveAlert(String alertMessage, RobotTelemetry robotTelemetry){
        sendAlert(AlertRobotTelemetry.builder().message(alertMessage).robotTelemetry(robotTelemetry).build());
        saveAlertInDB(alertMessage,robotTelemetry);
    }

    public void sendAlert(AlertRobotTelemetry robotTelemetry) {
        try {
            String payload = new ObjectMapper().writeValueAsString(robotTelemetry);
            streamHandler.broadcast(payload);
        } catch (JsonProcessingException e) {
            log.error("Create string from robot telemetry object : " + robotTelemetry);
            throw new RuntimeException(e);
        }
    }

    public void saveAlertInDB(String alertMessage, RobotTelemetry robotTelemetry) {
        AlertEntity alertEntity = AlertEntity.builder()
                .robotId(robotTelemetry.getRobot_id())
                .robotType(robotTelemetry.getRobot_type())
                .alertMessage(alertMessage)
                .telemetryData(robotTelemetry.getTelemetry().toString())
                .status("active")
                .build();
        robotTelemetryAdapter.saveAlert(alertEntity);
    }

    public List<Alert> getActiveAlerts() {
       return robotTelemetryAdapter.getActiveAlerts()
               .stream().map(RobotTelemetryMapper::entityToAlert).toList();
    }
}
