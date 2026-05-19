package dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper;

import dk.via.group1.urbanmicrofarm_backend.application.domain.GrowingSetup;
import dk.via.group1.urbanmicrofarm_backend.database.entities.GrowingSetupEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GrowingSetupDbMapper {

  public GrowingSetup toDomain(GrowingSetupEntity entity) {
    if (entity == null) {
      return null;
    }

    GrowingSetup setup = new GrowingSetup(
        entity.getSerialNumber(),
        entity.getLocation(),
        new ArrayList<>()
    );

    setup.setStatus("ACTIVE");

    return setup;
  }

  public GrowingSetupEntity toEntity(GrowingSetup domain) {
    if (domain == null) {
      return null;
    }

    GrowingSetupEntity entity = new GrowingSetupEntity();

    entity.setSerialNumber(domain.getSerialNumber());
    entity.setLocation(domain.getLocation());
    entity.setUserId(1L); // TODO: Replace with actual user ID logic

    return entity;
  }
}