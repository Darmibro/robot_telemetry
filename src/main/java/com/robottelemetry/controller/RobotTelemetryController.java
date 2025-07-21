package com.robottelemetry.controller;

import com.robottelemetry.dto.RobotTelemetry;
import com.robottelemetry.model.ActiveRobotResponse;
import com.robottelemetry.service.RobotTelemetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/robots")
@RequiredArgsConstructor
public class RobotTelemetryController {

    private final RobotTelemetryService robotTelemetryService;

    @GetMapping
    public ResponseEntity<List<ActiveRobotResponse>> listActiveRobots() {
        return ResponseEntity.ok(robotTelemetryService.getActiveRobots());
    }

    @GetMapping("/{id}/telemetry")
    public ResponseEntity<RobotTelemetry> getRobotTelemetry(@PathVariable String robotId) {
        return ResponseEntity.ok(robotTelemetryService.getTelemetryForRobot(robotId));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getRobotStatus(@PathVariable String robotId) {
        return ResponseEntity.ok(robotTelemetryService.getRobotStatus(robotId));
    }
}
