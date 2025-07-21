package com.robottelemetry.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TelemetryData {
    private double battery_level;
    private double cpu_usage;
    private double temperature;
    private double payload_weight;
    private String status;
    private List<Double> joint_positions;
    private String mission_id;

    private Location location;
    private double altitude;    // For aerial robots
    private double wind_speed;

}
