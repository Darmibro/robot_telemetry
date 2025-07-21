package com.robottelemetry.enums;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum RobotTypeEnum {
    GROUND_ROBOT("specialized_robot"),
    AERIAL_ROBOT("aerial_robot");

    private final String value;

    RobotTypeEnum(String value) {
        this.value = value;
    }

    private static final Map<String, RobotTypeEnum> typeMap = Stream.of(values())
            .collect(Collectors.toMap(RobotTypeEnum::getValue, rt -> rt));

    public static RobotTypeEnum fromValue(String value) {
        RobotTypeEnum type = typeMap.get(value.toLowerCase());
        if (type == null) {
            throw new IllegalArgumentException("Unknown robot type: " + value);
        }
        return type;
    }
}
