package com.robottelemetry.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.robottelemetry.service.RobotTelemetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class RobotTelemetryListener {

    private final RobotTelemetryService robotTelemetryService;

    @KafkaListener(topics = "ground-telemetry", groupId = "telemetry-group")
    public void consumeGround(String message) {
        log.info("Ground robot telemetry received: {}", message);
        robotTelemetryService.process(message);
    }

    @KafkaListener(topics = "aerial-telemetry", groupId = "telemetry-group")
    public void consumeAerial(String message) {
        log.info("Aerial robot telemetry received: {}", message);
        robotTelemetryService.process(message);
    }
    }
