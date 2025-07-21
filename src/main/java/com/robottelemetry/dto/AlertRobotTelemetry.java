package com.robottelemetry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlertRobotTelemetry {
    private String message;
    private RobotTelemetry robotTelemetry;
}
