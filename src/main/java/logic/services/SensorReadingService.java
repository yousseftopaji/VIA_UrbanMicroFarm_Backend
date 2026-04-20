package logic.services;

import dto.TelemetryData;

public interface SensorReadingService {
    void processReadings(TelemetryData telemetryData);
}
