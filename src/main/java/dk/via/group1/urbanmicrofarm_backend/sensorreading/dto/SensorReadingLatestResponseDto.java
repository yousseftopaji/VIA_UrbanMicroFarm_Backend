package dk.via.group1.urbanmicrofarm_backend.sensorreading.dto;

import java.time.Instant;

public record SensorReadingLatestResponseDto(String sensorId, Double value,
    Instant timestamp)
{
}
