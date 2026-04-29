package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;

import java.util.List;
import java.util.Optional;

public interface SensorReadingQueryService {
    Optional<SensorReading> getLatestReading(Integer setupId, String sensorType);

    List<SensorReading> getHistoricalReadings(Integer setupId, String sensorType);
}