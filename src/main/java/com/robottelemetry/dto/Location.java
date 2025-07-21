package com.robottelemetry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private double lat;
    private double lng;
}
