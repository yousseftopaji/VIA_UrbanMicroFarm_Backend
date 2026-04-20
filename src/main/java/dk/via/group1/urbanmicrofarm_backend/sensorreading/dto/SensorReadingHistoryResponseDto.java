package dk.via.group1.urbanmicrofarm_backend.sensorreading.dto;

import java.time.Instant;
import java.util.List;

public record SensorReadingHistoryResponseDto(String sensorId,
    List<SensorReadingHistoryItemDto> data)
{
  public record SensorReadingHistoryItemDto(Double value, Instant timestamp)
  {
  }
}
