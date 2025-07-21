package com.robottelemetry.validator;

import com.robottelemetry.dto.RobotTelemetry;
import com.robottelemetry.excaption.RobotTelemetryValidateException;

public class RobotTelemetryValidator {

    public static void validate(RobotTelemetry robotTelemetry){
            validateBattery(robotTelemetry.getTelemetry().getBattery_level());
            validateTemperature(robotTelemetry.getTelemetry().getTemperature());
            validateStatus(robotTelemetry.getTelemetry().getStatus());
    }
    private static void validateBattery(double battery){
    if (battery < 20 ) {
        throw new RobotTelemetryValidateException("Battery level out of range: " + battery);
    }
    }

    private static void validateTemperature(double temperature) {
       if (temperature > 60){
           throw new RobotTelemetryValidateException("High temperature detected: " + temperature + "°C");
       }
    }

    private static void validateStatus(String status){
        if ("operational".equals(status)){
            throw new RobotTelemetryValidateException("Status: " + status);
        }
    }
}
