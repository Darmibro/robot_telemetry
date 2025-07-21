package com.robottelemetry.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "robot_status")
public class RobotStatusEntity {
    @Id
    private String robotId;
    private String robotType;
    private String status;
    private ZonedDateTime updatedTimestamp;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updatedTimestamp = ZonedDateTime.now();
    }
}