package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.application.mapper.SensorReadingPersistenceMapper;
import dk.via.group1.urbanmicrofarm_backend.application.database.entities.SensorReadingEntity;
import dk.via.group1.urbanmicrofarm_backend.application.database.repository.SensorReadingRepository;
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

        Sensor temperatureSensor = new Sensor(telemetryData.getSensorId(), SensorType.TEMPERATURE, "C");
        Sensor humiditySensor = new Sensor(telemetryData.getSensorId(), SensorType.HUMIDITY, "%");
        Sensor lightSensor = new Sensor(telemetryData.getSensorId(), SensorType.LIGHT, "ADC");
        Sensor soilMoistureSensor = new Sensor(telemetryData.getSensorId(), SensorType.SOIL_MOISTURE, "ADC");

        readings.add(new SensorReading(temperatureSensor, telemetryData.getTemperature() / 10.0, timestamp));
        readings.add(new SensorReading(humiditySensor, telemetryData.getHumidity() / 10.0, timestamp));
        readings.add(new SensorReading(lightSensor, telemetryData.getLight(), timestamp));
        readings.add(new SensorReading(soilMoistureSensor, telemetryData.getSoilMoisture(), timestamp));

        List<SensorReadingEntity> entities = readings.stream()
                .map(reading -> sensorReadingPersistenceMapper.toEntity(telemetryData.getSetupId(), reading))
                .toList();

        sensorReadingRepository.saveAll(entities);
    }

    private void validate(TelemetryData telemetryData) {
        if (telemetryData.getSetupId() <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
        }

        if (telemetryData.getSensorId() == null || telemetryData.getSensorId() <= 0) {
            throw new IllegalArgumentException("Invalid sensor id");
        }
    }
}