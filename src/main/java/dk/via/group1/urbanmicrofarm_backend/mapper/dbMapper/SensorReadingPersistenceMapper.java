package dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper;


import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import org.springframework.stereotype.Component;

@Component
public class SensorReadingPersistenceMapper {

    public SensorReadingEntity toEntity(Long sensorId, SensorReading sensorReading) {
        SensorReadingEntity entity = new SensorReadingEntity();

        entity.setSensorId(sensorId);
        entity.setValue(sensorReading.getValue());
        entity.setTimestamp(sensorReading.getTimestamp());

        return entity;
    }

    public SensorReading toDomain(SensorReadingEntity entity) {
        Sensor sensor = new Sensor(
                entity.getSensorId().intValue(),
                SensorType.valueOf(entity.getSensor().getSensorTypeName()),
                entity.getSensor().getUnit()
        );

        return new SensorReading(
                sensor,
                entity.getValue(),
                entity.getTimestamp()
        );
    }
}
