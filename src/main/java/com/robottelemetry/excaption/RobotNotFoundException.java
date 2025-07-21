package com.robottelemetry.excaption;

public class RobotNotFoundException extends RuntimeException{
    public RobotNotFoundException(String robotId){
        super("Robot with id " + robotId + " not found.");
    }
}
