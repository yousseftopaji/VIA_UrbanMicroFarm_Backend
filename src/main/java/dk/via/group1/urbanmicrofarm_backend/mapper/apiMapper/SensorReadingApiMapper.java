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
            SensorReading sensorReading) {

        return new SensorReadingLatestResponseDto(
                sensorReading.getSensor().getSensorId(),
                sensorReading.getValue(),
                sensorReading.getTimestamp()
        );
    }

    public SensorReadingHistoryResponseDto toHistoryResponseDto(
            List<SensorReading> readings) {

        List<SensorReadingHistoryResponseDto.SensorReadingHistoryItemDto> data =
                readings.stream()
                        .map(reading -> new SensorReadingHistoryResponseDto.SensorReadingHistoryItemDto(
                                reading.getValue(),
                                reading.getTimestamp()
                        ))
                        .toList();

        return new SensorReadingHistoryResponseDto(
                readings.getFirst().getSensor().getSensorId(),
                data
        );
    }
}