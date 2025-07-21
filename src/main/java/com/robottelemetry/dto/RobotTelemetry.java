package com.robottelemetry.dto;

import lombok.Builder;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
@Builder
public class RobotTelemetry {
    private String robot_id;
    private String timestamp; //ZonedDateTime timestamp;
    private String robot_type;
    private TelemetryData telemetry;
}
