package Mapper;

import dto.databaseDTOs.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.Sensor;

public class SensorReadingMapper {

  public static dk.via.group1.urbanmicrofarm_backend.logic.domain.SensorReading toDomain(SensorReading dto, Sensor sensor) {
    return new dk.via.group1.urbanmicrofarm_backend.logic.domain.SensorReading(
        sensor,
        dto.getValue(),
        dto.getTimestamp()
    );
  }

  public static SensorReading toDto(dk.via.group1.urbanmicrofarm_backend.logic.domain.SensorReading domain) {
    return new SensorReading(
        0,
        domain.getSensor().getSensorId(),
        domain.getValue(),
        domain.getTimestamp()
    );
  }
}