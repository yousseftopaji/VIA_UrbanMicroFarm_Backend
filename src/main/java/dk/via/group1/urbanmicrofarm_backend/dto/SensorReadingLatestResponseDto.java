package dk.via.group1.urbanmicrofarm_backend.dto;

import java.time.Instant;

public record SensorReadingLatestResponseDto(
        Integer setupId,
        Integer sensorId,
        String sensorType,
        Double value,
        Instant timestamp
) {
}