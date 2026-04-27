package dk.via.group1.urbanmicrofarm_backend.logic.services;

import dto.TelemetryData;

public interface SensorReadingService {
    void processReadings(TelemetryData telemetryData);
}
