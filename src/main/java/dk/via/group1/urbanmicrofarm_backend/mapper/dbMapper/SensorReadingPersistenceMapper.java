package dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper;


import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class SensorReadingPersistenceMapper {

    public SensorReadingEntity toEntity(int setupId, SensorReading sensorReading) {
        SensorReadingEntity entity = new SensorReadingEntity();

        entity.setSetupId(setupId);
        entity.setSensorId(sensorReading.getSensor().getSensorId());
        entity.setSensorType(sensorReading.getSensor().getType().name());
        entity.setValue(sensorReading.getValue());
        entity.setTimestamp(sensorReading.getTimestamp().toInstant(ZoneOffset.UTC));

        return entity;
    }

    public SensorReading toDomain(SensorReadingEntity entity) {
        Sensor sensor = new Sensor(
                entity.getSensorId(),
                SensorType.valueOf(entity.getSensorType()),
                getUnitForSensorType(entity.getSensorType())
        );

        LocalDateTime timestamp = LocalDateTime.ofInstant(
                entity.getTimestamp(),
                ZoneOffset.UTC
        );

        return new SensorReading(
                sensor,
                entity.getValue(),
                timestamp
        );
    }

    private String getUnitForSensorType(String sensorType) {
        SensorType type = SensorType.valueOf(sensorType);

        return switch (type) {
            case TEMPERATURE -> "C";
            case HUMIDITY -> "%";
            case LIGHT -> "ADC";
            case SOIL_MOISTURE -> "ADC";
        };
    }
}
