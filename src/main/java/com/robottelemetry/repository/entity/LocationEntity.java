package com.robottelemetry.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@Builder
@Embeddable
public class LocationEntity {
    private double lat;
    private double lng;
}
