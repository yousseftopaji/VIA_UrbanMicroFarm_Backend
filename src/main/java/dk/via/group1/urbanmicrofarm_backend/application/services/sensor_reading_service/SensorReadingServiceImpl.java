package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorEntity;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorReadingRepository;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorRepository;
import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper.SensorReadingPersistenceMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingServiceImpl implements SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;
    private final SensorReadingPersistenceMapper sensorReadingPersistenceMapper;

    public SensorReadingServiceImpl(
            SensorReadingRepository sensorReadingRepository,
            SensorRepository sensorRepository,
            SensorReadingPersistenceMapper sensorReadingPersistenceMapper) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorRepository = sensorRepository;
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

        for (SensorReading reading : readings) {
            Optional<SensorEntity> sensor = sensorRepository.findFirstBySerialNumberAndSensorTypeName(telemetryData.serialNumber(), reading.getSensor().getType().name());
            if (sensor.isPresent()) {
                SensorReadingEntity entity = sensorReadingPersistenceMapper.toEntity(sensor.get().getId(), reading);
                sensorReadingRepository.save(entity);
            }
        }
    }

    private void validate(TelemetryData telemetryData) {
        if (telemetryData.serialNumber() == null || telemetryData.serialNumber().isBlank()) {
            throw new IllegalArgumentException("Invalid serial number");
        }

        if (telemetryData.sensorId() == null || telemetryData.sensorId() <= 0) {
            throw new IllegalArgumentException("Invalid sensor id");
        }
    }
}