package dk.via.group1.urbanmicrofarm_backend.logic.services;

import dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.SensorType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SensorReadingServiceImpl implements SensorReadingService {
    @Override
    public void processReadings(TelemetryData telemetryData) {
        validate(telemetryData);

        LocalDateTime timestamp = LocalDateTime.now();

        List<SensorReading> readings = new ArrayList<>();

        Sensor temperatureSensor = new Sensor(telemetryData.getSensorId(), SensorType.TEMPERATURE, "C");
        Sensor humiditySensor = new Sensor(telemetryData.getSensorId(), SensorType.HUMIDITY, "%");
        Sensor lightSensor = new Sensor(telemetryData.getSensorId(), SensorType.LIGHT, "ADC");
        Sensor soilMoistureSensor = new Sensor(telemetryData.getSensorId(), SensorType.SOIL_MOISTURE, "ADC");

        readings.add(new SensorReading(temperatureSensor, telemetryData.getTemperature() / 10.0, timestamp));
        readings.add(new SensorReading(humiditySensor, telemetryData.getHumidity() / 10.0, timestamp));
        readings.add(new SensorReading(lightSensor, telemetryData.getLight(), timestamp));
        readings.add(new SensorReading(soilMoistureSensor, telemetryData.getSoilMoisture(), timestamp));

        // TODO call database service to save the readings
    }





    private void validate(TelemetryData telemetryData) {
        if (telemetryData.getSetupId() <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
            //this method can be maybe later put to class on its own
            //maybe we can do like a util package where we will put all of our validation classes
        }
    }
}

