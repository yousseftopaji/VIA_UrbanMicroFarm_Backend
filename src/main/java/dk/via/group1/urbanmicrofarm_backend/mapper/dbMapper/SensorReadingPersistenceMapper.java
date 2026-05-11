package dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper;


import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import org.springframework.stereotype.Component;

@Component
public class SensorReadingPersistenceMapper {

    public SensorReadingEntity toEntity(int setupId, SensorReading sensorReading) {
        SensorReadingEntity entity = new SensorReadingEntity();

        entity.setSetupId(setupId);
        entity.setSensorId(sensorReading.getSensor().getSensorId());
        entity.setSensorType(sensorReading.getSensor().getType().name());
        entity.setValue(sensorReading.getValue());
        entity.setTimestamp(sensorReading.getTimestamp());

        return entity;
    }

    public SensorReading toDomain(SensorReadingEntity entity) {
        Sensor sensor = new Sensor(
                entity.getSensorId(),
                SensorType.valueOf(entity.getSensorType()),
                getUnitForSensorType(entity.getSensorType())
        );

        return new SensorReading(
                sensor,
                entity.getValue(),
                entity.getTimestamp()
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
