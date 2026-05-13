package dk.via.group1.urbanmicrofarm_backend.dto;

import java.time.Instant;
import java.util.List;

public record SensorReadingHistoryResponseDto(
        Integer sensorId,
        List<SensorReadingHistoryItemDto> data
) {
    public record SensorReadingHistoryItemDto(
            Double value,
            Instant timestamp
    ) {
    }
}