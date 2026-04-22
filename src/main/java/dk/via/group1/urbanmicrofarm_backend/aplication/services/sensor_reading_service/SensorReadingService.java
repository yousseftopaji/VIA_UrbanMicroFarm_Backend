package dk.via.group1.urbanmicrofarm_backend.aplication.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;

public interface SensorReadingService {
    void processReadings(TelemetryData telemetryData);
}
