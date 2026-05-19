package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorEntity;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorReadingRepository;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorRepository;
import dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper.SensorReadingPersistenceMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingQueryServiceImpl implements SensorReadingQueryService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;
    private final SensorReadingPersistenceMapper sensorReadingPersistenceMapper;

    public SensorReadingQueryServiceImpl(
            SensorReadingRepository sensorReadingRepository,
            SensorRepository sensorRepository,
            SensorReadingPersistenceMapper sensorReadingPersistenceMapper) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorRepository = sensorRepository;
        this.sensorReadingPersistenceMapper = sensorReadingPersistenceMapper;
    }

    @Override
    public Optional<SensorReading> getLatestReading(String serialNumber, String sensorType) {
        validateSerialNumber(serialNumber);
        String validSensorType = validateSensorType(sensorType);

        Optional<SensorEntity> sensor = sensorRepository.findFirstBySerialNumberAndSensorTypeName(serialNumber, validSensorType);
        if (sensor.isEmpty()) {
            return Optional.empty();
        }

        return sensorReadingRepository
                .findFirstBySensorIdOrderByTimestampDesc(sensor.get().getId())
                .map(sensorReadingPersistenceMapper::toDomain);
    }

    @Override
    public List<SensorReading> getHistoricalReadings(String serialNumber, String sensorType) {
        validateSerialNumber(serialNumber);
        String validSensorType = validateSensorType(sensorType);

        Optional<SensorEntity> sensor = sensorRepository.findFirstBySerialNumberAndSensorTypeName(serialNumber, validSensorType);
        if (sensor.isEmpty()) {
            return Collections.emptyList();
        }

        return sensorReadingRepository
                .findBySensorIdOrderByTimestampDesc(sensor.get().getId())
                .stream()
                .map(sensorReadingPersistenceMapper::toDomain)
                .toList();
    }

    private void validateSerialNumber(String serialNumber) {
        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException("Invalid serial number");
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