package com.robottelemetry.adapter;

import com.robottelemetry.dto.Alert;
import com.robottelemetry.dto.Location;
import com.robottelemetry.dto.RobotTelemetry;
import com.robottelemetry.dto.TelemetryData;
import com.robottelemetry.model.ActiveRobotResponse;
import com.robottelemetry.repository.entity.*;

import java.util.Arrays;
import java.util.List;

public class RobotTelemetryMapper {

    public static GroundRobotTelemetryEntity objectToGroundRobotEntity(RobotTelemetry robotTelemetry) {
        return GroundRobotTelemetryEntity.builder()
                .robotId(robotTelemetry.getRobot_id())
                .robotType(robotTelemetry.getRobot_type())
                .timestamp(robotTelemetry.getTimestamp())
                .batteryLevel(robotTelemetry.getTelemetry().getBattery_level())
                .cpuUsage(robotTelemetry.getTelemetry().getCpu_usage())
                .temperature(robotTelemetry.getTelemetry().getTemperature())
                .status(robotTelemetry.getTelemetry().getStatus())
                .missionId(robotTelemetry.getTelemetry().getMission_id())
                .jointPositions(robotTelemetry.getTelemetry().getJoint_positions().toString())
                .payloadWeight(robotTelemetry.getTelemetry().getPayload_weight())
                .build();
    }

    public static AerialRobotTelemetryEntity objectToAerialRobotEntity(RobotTelemetry robotTelemetry){
            return AerialRobotTelemetryEntity.builder()
                    .robotId(robotTelemetry.getRobot_id())
                    .robotType(robotTelemetry.getRobot_type())
                    .timestamp(robotTelemetry.getTimestamp())
                    .batteryLevel(robotTelemetry.getTelemetry().getBattery_level())
                    .cpuUsage(robotTelemetry.getTelemetry().getCpu_usage())
                    .temperature(robotTelemetry.getTelemetry().getTemperature())
                    .status(robotTelemetry.getTelemetry().getStatus())
                    .missionId(robotTelemetry.getTelemetry().getMission_id())
                    .altitude(robotTelemetry.getTelemetry().getAltitude())
                    .windSpeed(robotTelemetry.getTelemetry().getWind_speed())
                    .location(LocationEntity.builder().lat(robotTelemetry.getTelemetry().getLocation().getLat())
                            .lng(robotTelemetry.getTelemetry().getLocation().getLng())
                            .build())
                    .build();
    }

    public static RobotStatusEntity objectToRobotStatusEntity(String robotId, String robotStatus){
        return RobotStatusEntity.builder()
                .robotId(robotId)
                .status(robotStatus)
                .build();
    }

    public static ActiveRobotResponse entityToActiveRobotResponse(RobotStatusEntity entity){
        return ActiveRobotResponse.builder()
                .robot_id(entity.getRobotId())
                .robot_type(entity.getRobotType())
                .build();
    }

    public static RobotTelemetry groundRobotEntityToObject(GroundRobotTelemetryEntity entity) {
        List<Double> jointPositions = Arrays.stream(
                        entity.getJointPositions().replace("[", "").replace("]", "").split(","))
                .map(String::trim)
                .map(Double::parseDouble)
                .toList();
        return RobotTelemetry.builder()
                .robot_id(entity.getRobotId())
                .timestamp(entity.getTimestamp())
                .robot_type(entity.getRobotType())
                .telemetry(
                        TelemetryData.builder()
                                .battery_level(entity.getBatteryLevel())
                                .cpu_usage(entity.getCpuUsage())
                                .temperature(entity.getTemperature())
                                .status(entity.getStatus())
                                .mission_id(entity.getMissionId())
                                .payload_weight(entity.getPayloadWeight())
                                .joint_positions(jointPositions) // Assumes it's already a List<Double>
                                .build()
                ).build();
    }

    public static RobotTelemetry aerialRobotEntityToObject(AerialRobotTelemetryEntity entity) {
        return RobotTelemetry.builder()
                .robot_id(entity.getRobotId())
                .timestamp(entity.getTimestamp())
                .robot_type(entity.getRobotType())
                .telemetry(
                        TelemetryData.builder()
                                .battery_level(entity.getBatteryLevel())
                                .cpu_usage(entity.getCpuUsage())
                                .temperature(entity.getTemperature())
                                .status(entity.getStatus())
                                .mission_id(entity.getMissionId())
                                .altitude(entity.getAltitude())
                                .wind_speed(entity.getWindSpeed())
                                .location(
                                        Location.builder()
                                                .lat(entity.getLocation().getLat())
                                                .lng(entity.getLocation().getLng())
                                                .build()
                                )
                                .build()
                ).build();
    }

    public static Alert entityToAlert(AlertEntity entity){
        return Alert.builder()
                .id(entity.getId())
                .robotId(entity.getRobotId())
                .robotType(entity.getRobotType())
                .alertMessage(entity.getAlertMessage())
                .telemetryData(entity.getTelemetryData())
                .createTimestamp(entity.getCreateTimestamp())
                .build();
    }

}

