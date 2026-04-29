package dk.via.group1.urbanmicrofarm_backend.mapper.apiMapper;

import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingHistoryResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingLatestResponseDto;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

@Component
public class SensorReadingApiMapper {

    public SensorReadingLatestResponseDto toLatestResponseDto(
            Integer setupId,
            SensorReading sensorReading) {

        return new SensorReadingLatestResponseDto(
                setupId,
                sensorReading.getSensor().getSensorId(),
                sensorReading.getSensor().getType().name(),
                sensorReading.getValue(),
                sensorReading.getTimestamp().toInstant(ZoneOffset.UTC)
        );
    }

    public SensorReadingHistoryResponseDto toHistoryResponseDto(
            Integer setupId,
            String sensorType,
            List<SensorReading> readings) {

        List<SensorReadingHistoryResponseDto.SensorReadingHistoryItemDto> data =
                readings.stream()
                        .map(reading -> new SensorReadingHistoryResponseDto.SensorReadingHistoryItemDto(
                                reading.getValue(),
                                reading.getTimestamp().toInstant(ZoneOffset.UTC)
                        ))
                        .toList();

        return new SensorReadingHistoryResponseDto(
                setupId,
                sensorType.toUpperCase(),
                data
        );
    }
}