package com.robottelemetry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ActiveRobotResponse {

    private String robot_id;
    private String robot_type;

}
