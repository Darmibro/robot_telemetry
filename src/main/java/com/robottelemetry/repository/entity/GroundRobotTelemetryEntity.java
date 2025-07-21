package com.robottelemetry.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@Entity
@Table(name = "ground_telemetry")
public class GroundRobotTelemetryEntity extends BaseRobotTelemetryEntity {

    private double payloadWeight;
    private String jointPositions;
}

