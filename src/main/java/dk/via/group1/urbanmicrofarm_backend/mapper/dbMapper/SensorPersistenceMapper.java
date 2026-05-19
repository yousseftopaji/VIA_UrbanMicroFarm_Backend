package dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper;

import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorEntity;
import org.springframework.stereotype.Component;

@Component
public class SensorPersistenceMapper {

  public SensorEntity toEntity(String serialNumber, Sensor sensor) {
    SensorEntity entity = new SensorEntity();
    entity.setSerialNumber(serialNumber);
    entity.setUnit(sensor.getUnit());
    entity.setSensorTypeName(sensor.getType().name());
    return entity;
  }

  public Sensor toDomain(SensorEntity entity) {
    return new Sensor(
        entity.getId().intValue(),
        SensorType.valueOf(entity.getSensorTypeName()),
        entity.getUnit()
    );
  }
}
