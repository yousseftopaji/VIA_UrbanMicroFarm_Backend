package Mapper;

import dto.databaseDTOs.Sensor;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.SensorType;

public class SensorMapper {

  public static dk.via.group1.urbanmicrofarm_backend.logic.domain.Sensor toDomain(Sensor dto) {
    return new dk.via.group1.urbanmicrofarm_backend.logic.domain.Sensor(
        dto.getSensorId(),
        SensorType.valueOf(dto.getType()),
        dto.getUnit()
    );
  }

  public static Sensor toDto(dk.via.group1.urbanmicrofarm_backend.logic.domain.Sensor domain) {
    return new Sensor(
        domain.getSensorId(),
        domain.getType().name(),
        domain.getUnit(),
        0
    );
  }
}