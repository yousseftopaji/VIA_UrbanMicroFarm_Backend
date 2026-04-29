package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorReadingRepository;
import dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper.SensorReadingPersistenceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingQueryServiceImpl implements SensorReadingQueryService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorReadingPersistenceMapper sensorReadingPersistenceMapper;

    public SensorReadingQueryServiceImpl(
            SensorReadingRepository sensorReadingRepository,
            SensorReadingPersistenceMapper sensorReadingPersistenceMapper) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorReadingPersistenceMapper = sensorReadingPersistenceMapper;
    }

    @Override
    public Optional<SensorReading> getLatestReading(Integer setupId, String sensorType) {
        validateSetupId(setupId);
        String validSensorType = validateSensorType(sensorType);

        return sensorReadingRepository
                .findFirstBySetupIdAndSensorTypeOrderByTimestampDesc(setupId, validSensorType)
                .map(sensorReadingPersistenceMapper::toDomain);
    }

    @Override
    public List<SensorReading> getHistoricalReadings(Integer setupId, String sensorType) {
        validateSetupId(setupId);
        String validSensorType = validateSensorType(sensorType);

        return sensorReadingRepository
                .findBySetupIdAndSensorTypeOrderByTimestampDesc(setupId, validSensorType)
                .stream()
                .map(sensorReadingPersistenceMapper::toDomain)
                .toList();
    }

    private void validateSetupId(Integer setupId) {
        if (setupId == null || setupId <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
        }
    }

    private String validateSensorType(String sensorType) {
        if (sensorType == null || sensorType.isBlank()) {
            throw new IllegalArgumentException("Sensor type is required");
        }

        try {
            return SensorType.valueOf(sensorType.toUpperCase()).name();
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid sensor type: " + sensorType);
        }
    }
}