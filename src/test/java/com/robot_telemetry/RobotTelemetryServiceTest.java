package com.robot_telemetry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robottelemetry.adapter.RobotTelemetryAdapter;
import com.robottelemetry.adapter.RobotTelemetryMapper;
import com.robottelemetry.dto.Location;
import com.robottelemetry.dto.RobotTelemetry;
import com.robottelemetry.dto.TelemetryData;
import com.robottelemetry.excaption.RobotTelemetryValidateException;
import com.robottelemetry.model.ActiveRobotResponse;
import com.robottelemetry.repository.entity.AerialRobotTelemetryEntity;
import com.robottelemetry.repository.entity.GroundRobotTelemetryEntity;
import com.robottelemetry.repository.entity.RobotStatusEntity;
import com.robottelemetry.service.AlertService;
import com.robottelemetry.service.RobotTelemetryService;
import com.robottelemetry.validator.RobotTelemetryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RobotTelemetryServiceTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AlertService alertService;
    @Mock
    private RobotTelemetryAdapter adapter;

    @InjectMocks
    private RobotTelemetryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void process_validSpecializedRobotTelemetry_savesGroundTelemetryAndUpdatesStatus() throws Exception {
        String json = "{\"robot_id\":\"r1\",\"robot_type\":\"specialized_robot\",\"telemetry\":{\"status\":\"operational\"}}";
        RobotTelemetry telemetry = createRobot();
        telemetry.setRobot_id("r1");
        telemetry.setRobot_type("specialized_robot");
        TelemetryData t = createTelemtryData();
        t.setStatus("operational");
        telemetry.setTelemetry(t);

        when(objectMapper.readValue(json, RobotTelemetry.class)).thenReturn(telemetry);

        // Static mock for validator
        try (MockedStatic<RobotTelemetryValidator> validatorMock = mockStatic(RobotTelemetryValidator.class)) {
            service.process(json);
            verify(adapter).saveGroundRobotTelemetry(any());
            verify(adapter).updateRobotStatus(any());
            verify(alertService, never()).sendAndSaveAlert(anyString(), any());
            validatorMock.verify(() -> RobotTelemetryValidator.validate(telemetry));
        }
    }

    @Test
    void process_validAerialRobotTelemetry_savesAerialTelemetryAndUpdatesStatus() throws Exception {
        String json = "{\"robot_id\":\"r2\",\"robot_type\":\"aerial_robot\",\"telemetry\":{\"status\":\"operational\"}}";
        RobotTelemetry telemetry = createRobot();
        telemetry.setRobot_id("r2");
        telemetry.setRobot_type("aerial_robot");
        TelemetryData t = createTelemtryData();
        t.setStatus("operational");
        telemetry.setTelemetry(t);

        when(objectMapper.readValue(json, RobotTelemetry.class)).thenReturn(telemetry);

        try (MockedStatic<RobotTelemetryValidator> validatorMock = mockStatic(RobotTelemetryValidator.class)) {
            service.process(json);
            verify(adapter).saveAerialRobotTelemetry(any());
            verify(adapter).updateRobotStatus(any());
            verify(alertService, never()).sendAndSaveAlert(anyString(), any());
            validatorMock.verify(() -> RobotTelemetryValidator.validate(telemetry));
        }
    }

    @Test
    void process_invalidTelemetry_sendsAlertAndSetsStatusCritical() throws Exception {
        String json = "{\"robot_id\":\"r3\",\"robot_type\":\"specialized_robot\",\"telemetry\":{\"status\":\"fail\"}}";
        RobotTelemetry telemetry = createRobot();
        telemetry.setRobot_id("r3");
        telemetry.setRobot_type("specialized_robot");
        TelemetryData t = createTelemtryData();
        t.setStatus("fail");
        telemetry.setTelemetry(t);

        when(objectMapper.readValue(json, RobotTelemetry.class)).thenReturn(telemetry);

        try (MockedStatic<RobotTelemetryValidator> validatorMock = mockStatic(RobotTelemetryValidator.class)) {
            validatorMock.when(() -> RobotTelemetryValidator.validate(telemetry))
                    .thenThrow(new RobotTelemetryValidateException("Invalid"));

            service.process(json);

            verify(alertService).sendAndSaveAlert("Invalid", telemetry);
            verify(adapter).updateRobotStatus(any());
        }
    }

    @Test
    void process_jsonParseError_throwsRuntimeException() throws Exception {
        String json = "invalid";
        when(objectMapper.readValue(json, RobotTelemetry.class)).thenThrow(new JsonProcessingException("error") {
        });

        assertThrows(RuntimeException.class, () -> service.process(json));
    }

    @Test
    void getActiveRobots_returnsMappedResponses() {
        RobotStatusEntity robotEntity = new RobotStatusEntity();
        robotEntity.setRobotId("r1");
        robotEntity.setStatus("operational");

        when(adapter.getAllRobotsByStatus("operational")).thenReturn(List.of(robotEntity));
        try (MockedStatic<RobotTelemetryMapper> mapperMock = mockStatic(RobotTelemetryMapper.class)) {
            ActiveRobotResponse response = ActiveRobotResponse.builder().robot_id("r1").robot_type("").build();
            mapperMock.when(() -> RobotTelemetryMapper.entityToActiveRobotResponse(robotEntity)).thenReturn(response);

            List<ActiveRobotResponse> result = service.getActiveRobots();

            assertEquals(1, result.size());
            assertEquals("r1", result.get(0).getRobot_id());
        }
    }

    @Test
    void getTelemetryForRobot_specializedRobot_returnsGroundTelemetry() {
        RobotStatusEntity entity = mock(RobotStatusEntity.class);
        when(adapter.getRobotById("r1")).thenReturn(entity);
        when(entity.getRobotType()).thenReturn("specialized_robot");

        GroundRobotTelemetryEntity groundEntity = new GroundRobotTelemetryEntity();
        when(adapter.getTelemetryForGroundRobot("r1")).thenReturn(groundEntity);

        try (MockedStatic<RobotTelemetryMapper> mapperMock = mockStatic(RobotTelemetryMapper.class)) {
            RobotTelemetry telemetry = createRobot();
            mapperMock.when(() -> RobotTelemetryMapper.groundRobotEntityToObject(groundEntity)).thenReturn(telemetry);

            RobotTelemetry result = service.getTelemetryForRobot("r1");
            assertEquals(telemetry, result);
        }
    }

    @Test
    void getTelemetryForRobot_aerialRobot_returnsAerialTelemetry() {
        RobotStatusEntity entity = mock(RobotStatusEntity.class);
        when(adapter.getRobotById("r2")).thenReturn(entity);
        when(entity.getRobotType()).thenReturn("aerial_robot");

        AerialRobotTelemetryEntity aerialEntity = new AerialRobotTelemetryEntity();
        when(adapter.getTelemetryForAerialRobot("r2")).thenReturn(aerialEntity);

        try (MockedStatic<RobotTelemetryMapper> mapperMock = mockStatic(RobotTelemetryMapper.class)) {
            RobotTelemetry telemetry = createRobot();
            mapperMock.when(() -> RobotTelemetryMapper.aerialRobotEntityToObject(aerialEntity)).thenReturn(telemetry);

            RobotTelemetry result = service.getTelemetryForRobot("r2");
            assertEquals(telemetry, result);
        }
    }

    @Test
    void getTelemetryForRobot_unknownType_throwsException() {
        RobotStatusEntity entity = mock(RobotStatusEntity.class);
        when(adapter.getRobotById("rX")).thenReturn(entity);
        when(entity.getRobotType()).thenReturn("unknown_type");

        assertThrows(IllegalArgumentException.class, () -> service.getTelemetryForRobot("rX"));
    }

    @Test
    void getRobotStatus_returnsStatus() {
        RobotStatusEntity entity = mock(RobotStatusEntity.class);
        when(adapter.getRobotById("r1")).thenReturn(entity);
        when(entity.getStatus()).thenReturn("ok");

        assertEquals("ok", service.getRobotStatus("r1"));
    }

    private RobotTelemetry createRobot() {
        return RobotTelemetry.builder().
                robot_id("r1")
                .robot_type("specialized_robot")
                .telemetry(createTelemtryData())
                .build();


    }

    private TelemetryData createTelemtryData() {
        return TelemetryData.builder()
                .battery_level(95.5)
                .cpu_usage(23.8)
                .temperature(38.2)
                .payload_weight(12.5)
                .status("operational")
                .joint_positions(List.of(0.0, 1.1, 2.2))
                .mission_id("mission123")
                .location(
                        Location.builder()
                                .lat(51.5074)
                                .lng(-0.1278)
                                .build()
                )
                .altitude(350.0)
                .wind_speed(5.4)
                .build();
    }
}
