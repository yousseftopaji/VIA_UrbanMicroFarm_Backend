package dk.via.group1.urbanmicrofarm_backend.sensorreading.mapper;

import dk.via.group1.urbanmicrofarm_backend.sensorreading.dto.SensorReadingHistoryResponseDto;
import dk.via.group1.urbanmicrofarm_backend.sensorreading.dto.SensorReadingLatestResponseDto;
import dk.via.group1.urbanmicrofarm_backend.sensorreading.model.SensorReading;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorReadingApiMapper
{
  public SensorReadingLatestResponseDto toLatestResponseDto(
      SensorReading sensorReading)
  {
    return new SensorReadingLatestResponseDto(sensorReading.getSensorId(),
        sensorReading.getValue(), sensorReading.getTimestamp());
  }

  public SensorReadingHistoryResponseDto toHistoryResponseDto(String sensorId,
      List<SensorReading> sensorReadings)
  {
    List<SensorReadingHistoryResponseDto.SensorReadingHistoryItemDto> data = sensorReadings.stream()
        .map(reading -> new SensorReadingHistoryResponseDto.SensorReadingHistoryItemDto(
            reading.getValue(), reading.getTimestamp()))
        .toList();

    return new SensorReadingHistoryResponseDto(sensorId, data);
  }
}
