package dk.via.group1.urbanmicrofarm_backend.logic.services;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.SensorType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SensorReadingServiceImpl implements SensorReadingService {
    @Override
    public void processReadings(TelemetryData telemetryData) {
        validate(telemetryData);

        LocalDateTime timestamp = LocalDateTime.now();

        List<SensorReading> readings = new ArrayList<>();

        Sensor temperatureSensor = new Sensor(telemetryData.sensorId(), SensorType.TEMPERATURE, "C");
        Sensor humiditySensor = new Sensor(telemetryData.sensorId(), SensorType.HUMIDITY, "%");
        Sensor lightSensor = new Sensor(telemetryData.sensorId(), SensorType.LIGHT, "ADC");
        Sensor soilMoistureSensor = new Sensor(telemetryData.sensorId(), SensorType.SOIL_MOISTURE, "ADC");

        readings.add(new SensorReading(temperatureSensor, telemetryData.temperature() / 10.0, timestamp));
        readings.add(new SensorReading(humiditySensor, telemetryData.humidity() / 10.0, timestamp));
        readings.add(new SensorReading(lightSensor, telemetryData.light(), timestamp));
        readings.add(new SensorReading(soilMoistureSensor, telemetryData.soilMoisture(), timestamp));

        // TODO call database service to save the readings
    }





    private void validate(TelemetryData telemetryData) {
        if (telemetryData.setupId() <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
            //this method can be maybe later put to class on its own
            //maybe we can do like a util package where we will put all of our validation classes
        }
    }
}

