package dk.via.group1.urbanmicrofarm_backend.mapper;

import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingHistoryResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingLatestResponseDto;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorReadingApiMapper {

    public SensorReadingLatestResponseDto toLatestResponseDto(SensorReadingEntity sensorReading) {
        return new SensorReadingLatestResponseDto(
                sensorReading.getSetupId(),
                sensorReading.getSensorId(),
                sensorReading.getSensorType(),
                sensorReading.getValue(),
                sensorReading.getTimestamp()
        );
    }

    public SensorReadingHistoryResponseDto toHistoryResponseDto(
            Integer setupId,
            String sensorType,
            List<SensorReadingEntity> sensorReadings) {

        List<SensorReadingHistoryResponseDto.SensorReadingHistoryItemDto> data = sensorReadings.stream()
                .map(reading -> new SensorReadingHistoryResponseDto.SensorReadingHistoryItemDto(
                        reading.getValue(),
                        reading.getTimestamp()
                ))
                .toList();

        return new SensorReadingHistoryResponseDto(setupId, sensorType, data);
    }
}