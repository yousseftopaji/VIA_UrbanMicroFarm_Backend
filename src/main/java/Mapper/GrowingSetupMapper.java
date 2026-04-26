package Mapper;

import dto.databaseDTOs.GrowingSetup;
import dk.via.group1.urbanmicrofarm_backend.logic.domain.Sensor;
import java.util.List;

public class GrowingSetupMapper {

  public static dk.via.group1.urbanmicrofarm_backend.logic.domain.GrowingSetup toDomain(GrowingSetup dto, List<Sensor> sensors) {
    return new dk.via.group1.urbanmicrofarm_backend.logic.domain.GrowingSetup(
        dto.getSetupId(),
        dto.getSerialNumber(),
        dto.getLocation(),
        sensors
    );
  }

  public static GrowingSetup toDto(dk.via.group1.urbanmicrofarm_backend.logic.domain.GrowingSetup domain) {
    return new GrowingSetup(
        domain.getSetupId(),
        domain.getSerialNumber(),
        domain.getLocation()
    );
  }
}