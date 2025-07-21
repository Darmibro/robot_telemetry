package com.robottelemetry.repository.entity;

import com.robottelemetry.dto.TelemetryData;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name ="alerts")
public class AlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String robotId;
    private String robotType;
    private String alertMessage;
    private String telemetryData;
    private String status;
    private ZonedDateTime createTimestamp;

    @PrePersist
    public void createTimestamp() {
        this.createTimestamp = ZonedDateTime.now();
    }

}
