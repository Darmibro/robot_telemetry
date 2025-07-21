package com.robottelemetry.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@MappedSuperclass
public class BaseRobotTelemetryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String robotId;
    private String timestamp;
    private String robotType;

    private double batteryLevel;
    private double cpuUsage;
    private double temperature;
    private String status;
    private String missionId;



}
