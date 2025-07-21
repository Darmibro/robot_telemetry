package com.robottelemetry.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robottelemetry.adapter.RobotTelemetryAdapter;
import com.robottelemetry.adapter.RobotTelemetryMapper;
import com.robottelemetry.dto.RobotTelemetry;
import com.robottelemetry.excaption.RobotTelemetryValidateException;
import com.robottelemetry.model.ActiveRobotResponse;
import com.robottelemetry.validator.RobotTelemetryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotTelemetryService {

    private final ObjectMapper objectMapper;
    private final AlertService alertService;
    private final RobotTelemetryAdapter adapter;

    public void process(String message) {
        RobotTelemetry robotTelemetry = parseToRobotTelemetry(message);
        try {
            RobotTelemetryValidator.validate(robotTelemetry);

            saveRobotTelemetry(robotTelemetry);
            updateRobotStatus(robotTelemetry.getRobot_id(), robotTelemetry.getTelemetry().getStatus());

        } catch (RobotTelemetryValidateException e) {
            alertService.sendAndSaveAlert(e.getMessage(), robotTelemetry);
            updateRobotStatus(robotTelemetry.getRobot_id(), "critical");
        }

    }

    private RobotTelemetry parseToRobotTelemetry(String message) {
        try {
            return objectMapper.readValue(message, RobotTelemetry.class);
        } catch (JsonProcessingException e) {
            log.error("Parsing error: {}", message);
            throw new RuntimeException(e);
        }
    }

    private void saveRobotTelemetry(RobotTelemetry robotTelemetry) {
        if ("specialized_robot".equals(robotTelemetry.getRobot_type())) {
            adapter.saveGroundRobotTelemetry(RobotTelemetryMapper.objectToGroundRobotEntity(robotTelemetry));
        } else if ("aerial_robot".equals(robotTelemetry.getRobot_type())) {
            adapter.saveAerialRobotTelemetry(RobotTelemetryMapper.objectToAerialRobotEntity(robotTelemetry));
        }
    }

    private void updateRobotStatus(String robotId, String robotStatus) {
        adapter.updateRobotStatus(RobotTelemetryMapper.objectToRobotStatusEntity(robotId, robotStatus));
    }


    public List<ActiveRobotResponse> getActiveRobots() {
        return adapter.getAllRobotsByStatus("operational")
                .stream().map(RobotTelemetryMapper::entityToActiveRobotResponse).toList();
    }

    public RobotTelemetry getTelemetryForRobot(String robotId) {
        var robotType = adapter.getRobotById(robotId).getRobotType();
        return switch (robotType) {
            case "specialized_robot" ->
                    RobotTelemetryMapper.groundRobotEntityToObject(adapter.getTelemetryForGroundRobot(robotId));
            case "aerial_robot" ->
                    RobotTelemetryMapper.aerialRobotEntityToObject(adapter.getTelemetryForAerialRobot(robotId));
            default ->
                    throw new IllegalArgumentException("Unknown robot type: " + robotType);
        };
    }

    public String getRobotStatus(String robotId) {
        return adapter.getRobotById(robotId).getStatus();
    }
}
