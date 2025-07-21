package com.robottelemetry.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class Alert {

    private Long id;
    private String robotId;
    private String robotType;
    private String alertMessage;
    private String telemetryData;
    private ZonedDateTime createTimestamp;
}
