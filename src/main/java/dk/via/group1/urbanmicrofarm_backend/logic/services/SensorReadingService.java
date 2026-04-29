package dk.via.group1.urbanmicrofarm_backend.logic.services;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;

public interface SensorReadingService {
    void processReadings(TelemetryData telemetryData);
}
