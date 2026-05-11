package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import com.google.type.DateTime;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorReadingRepository;
import dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper.SensorReadingPersistenceMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public Optional<SensorReading> getLatestReading(Integer sensorId) {
        validateSensorId(sensorId);

        return sensorReadingRepository
                .findFirstBySensorIdOrderByTimestampDesc(sensorId)
                .map(sensorReadingPersistenceMapper::toDomain);
    }

    @Override
    public List<SensorReading> getHistoricalReadings(Integer sensorId, Instant from, Instant to) {
        validateSensorId(sensorId);

        return sensorReadingRepository
                .findBySensorIdAndTimeRange(sensorId, from, to)
                .stream()
                .map(sensorReadingPersistenceMapper::toDomain)
                .toList();
    }

    private void validateSensorId(Integer sensorId) {
        if (sensorId == null || sensorId <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
        }

//        TODO: fetch sensorId from database and check if exists
    }
}