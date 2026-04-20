package logic.services;

import dto.TelemetryData;
import logic.domain.SensorReading;
import logic.domain.SensorType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SensorReadingServiceImpl implements SensorReadingService {
    @Override
    public void processReadings(TelemetryData telemetryData) {
        validate(telemetryData);

        LocalDateTime timestamp = LocalDateTime.now();

        List<SensorReading> readings = new ArrayList<>();

        readings.add(new SensorReading(SensorType.TEMPERATURE, telemetryData.getTemperature() / 10.0, timestamp));
        readings.add(new SensorReading(SensorType.HUMIDITY, telemetryData.getHumidity() / 10.0, timestamp));
        readings.add(new SensorReading(SensorType.LIGHT, telemetryData.getLight(), timestamp));
        readings.add(new SensorReading(SensorType.SOIL_MOISTURE, telemetryData.getSoilMoisture(), timestamp));

        //TODO call database service to save
    }





    private void validate(TelemetryData telemetryData) {
        if (telemetryData.getSetupId() <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
        }
    }
}

