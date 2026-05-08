package dk.via.group1.urbanmicrofarm_backend.application.services.watering;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;

public interface WateringAutomationService {
    void handleWateringIfNeeded(TelemetryData telemetryData);
}
