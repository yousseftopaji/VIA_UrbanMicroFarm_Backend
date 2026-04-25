package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.application.database.entities.SensorReadingEntity;
import dk.via.group1.urbanmicrofarm_backend.application.database.repository.SensorReadingRepository;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingQueryService {

    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingQueryService(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    public Optional<SensorReadingEntity> getLatestReading(Integer setupId, String sensorType) {
        String validSensorType = validateSensorType(sensorType);

        return sensorReadingRepository.findFirstBySetupIdAndSensorTypeOrderByTimestampDesc(
                setupId,
                validSensorType
        );
    }

    public List<SensorReadingEntity> getHistoricalReadings(Integer setupId, String sensorType) {
        String validSensorType = validateSensorType(sensorType);

        return sensorReadingRepository.findBySetupIdAndSensorTypeOrderByTimestampDesc(
                setupId,
                validSensorType
        );
    }

    private String validateSensorType(String sensorType) {
        try {
            return SensorType.valueOf(sensorType.toUpperCase()).name();
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid sensor type: " + sensorType);
        }
    }
}