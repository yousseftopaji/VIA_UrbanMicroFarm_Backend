package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;

import java.util.List;
import java.util.Optional;

public interface SensorReadingQueryService {
    Optional<SensorReading> getLatestReading(String serialNumber, String sensorType);

    List<SensorReading> getHistoricalReadings(String serialNumber, String sensorType);
}