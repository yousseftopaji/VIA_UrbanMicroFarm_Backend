package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.application.services.watering.WateringAutomationService;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorReadingRepository;
import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper.SensorReadingPersistenceMapper;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorReadingServiceImpl implements SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorReadingPersistenceMapper sensorReadingPersistenceMapper;
    private final WateringAutomationService wateringAutomationService;

    public SensorReadingServiceImpl(
            SensorReadingRepository sensorReadingRepository,
            SensorReadingPersistenceMapper sensorReadingPersistenceMapper,
            WateringAutomationService wateringAutomationService) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorReadingPersistenceMapper = sensorReadingPersistenceMapper;
        this.wateringAutomationService = wateringAutomationService;
    }

    @Override
    public void processReadings(TelemetryData telemetryData) {
        validate(telemetryData); // we check the data first

        Instant timestamp = Instant.now(); // we make timestamp of time now

        // we get the values from telemetryData and we convert it
        double temperature = telemetryData.temperature() / 10.0;
        double humidity = telemetryData.humidity() / 10.0;
        int light = telemetryData.light();
        int soilMoistureRaw = telemetryData.soilMoisture();

        // we will make our list of sensor readings
        List<SensorReading> readings = createSensorReadings(
                telemetryData,
                timestamp,
                temperature,
                humidity,
                light,
                soilMoistureRaw
        );

        // we map our list of readings to db entities
        List<SensorReadingEntity> entities = readings.stream()
                .map(reading -> sensorReadingPersistenceMapper.toEntity(telemetryData.setupId(), reading))
                .toList();

        sensorReadingRepository.saveAll(entities); // we save the readings to db


        // after saving the readings, we check if watering is needed
        // this will call ML prediction and publish MQTT command if soil is too dry
        wateringAutomationService.handleWateringIfNeeded(telemetryData);
    }

    // helping method to create the list of sensor readings
    private List<SensorReading> createSensorReadings(
            TelemetryData telemetryData,
            Instant timestamp,
            double temperature,
            double humidity,
            int light,
            int soilMoistureRaw) {

        List<SensorReading> readings = new ArrayList<>(); // we create empty list of sensor readings

        // we create entity of Sensor
        Sensor temperatureSensor = new Sensor(telemetryData.sensorId(), SensorType.TEMPERATURE, "C");
        Sensor humiditySensor = new Sensor(telemetryData.sensorId(), SensorType.HUMIDITY, "%");
        Sensor lightSensor = new Sensor(telemetryData.sensorId(), SensorType.LIGHT, "ADC");
        Sensor soilMoistureSensor = new Sensor(telemetryData.sensorId(), SensorType.SOIL_MOISTURE, "ADC");

        // and we create and add readings to the list
        readings.add(new SensorReading(temperatureSensor, temperature, timestamp));
        readings.add(new SensorReading(humiditySensor, humidity, timestamp));
        readings.add(new SensorReading(lightSensor, light, timestamp));
        readings.add(new SensorReading(soilMoistureSensor, soilMoistureRaw, timestamp));

        return readings;
    }

    // helping method to validate incoming telemetry data
    private void validate(TelemetryData telemetryData) {
        if (telemetryData.setupId() <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
        }

        // sensor id can be null for now because contract allows null until multiple sensors are used
        if (telemetryData.sensorId() != null && telemetryData.sensorId() <= 0) {
            throw new IllegalArgumentException("Invalid sensor id");
        }
    }
}