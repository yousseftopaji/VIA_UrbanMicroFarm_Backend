package dk.via.group1.urbanmicrofarm_backend.dto;

import java.time.Instant;

public record SensorReadingLatestResponseDto(
        Integer sensorId,
        Double value,
        Instant timestamp
) {
}