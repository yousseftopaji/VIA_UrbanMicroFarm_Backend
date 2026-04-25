package dk.via.group1.urbanmicrofarm_backend.aplication.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.aplication.database.entities.SensorReadingEntity;
import dk.via.group1.urbanmicrofarm_backend.aplication.database.repository.SensorReadingRepository;
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
        return sensorReadingRepository.findFirstBySetupIdAndSensorTypeOrderByTimestampDesc(
                setupId,
                sensorType.toUpperCase()
        );
    }

    public List<SensorReadingEntity> getHistoricalReadings(Integer setupId, String sensorType) {
        return sensorReadingRepository.findBySetupIdAndSensorTypeOrderByTimestampDesc(
                setupId,
                sensorType.toUpperCase()
        );
    }
}