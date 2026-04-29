package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorReadingRepository;
import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper.SensorReadingPersistenceMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorReadingServiceImpl implements SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorReadingPersistenceMapper sensorReadingPersistenceMapper;

    public SensorReadingServiceImpl(
            SensorReadingRepository sensorReadingRepository,
            SensorReadingPersistenceMapper sensorReadingPersistenceMapper) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorReadingPersistenceMapper = sensorReadingPersistenceMapper;
    }

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

        List<SensorReadingEntity> entities = readings.stream()
                .map(reading -> sensorReadingPersistenceMapper.toEntity(telemetryData.setupId(), reading))
                .toList();

        sensorReadingRepository.saveAll(entities);
    }

    private void validate(TelemetryData telemetryData) {
        if (telemetryData.setupId() <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
        }

        if (telemetryData.sensorId() == null || telemetryData.sensorId() <= 0) {
            throw new IllegalArgumentException("Invalid sensor id");
        }
    }
}