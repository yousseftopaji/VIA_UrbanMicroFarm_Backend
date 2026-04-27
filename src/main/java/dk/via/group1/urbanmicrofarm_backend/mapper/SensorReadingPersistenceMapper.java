package dk.via.group1.urbanmicrofarm_backend.mapper;


import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import org.springframework.stereotype.Component;

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
}
