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
@Table(name = "aerial_telemetry")
public class AerialRobotTelemetryEntity extends BaseRobotTelemetryEntity {

    private double altitude;
    private double windSpeed;
    @Embedded
    private LocationEntity location;




}
