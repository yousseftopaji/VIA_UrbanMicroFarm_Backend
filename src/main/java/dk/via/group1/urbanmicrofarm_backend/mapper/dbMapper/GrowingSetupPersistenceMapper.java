package dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper;

import dk.via.group1.urbanmicrofarm_backend.application.domain.GrowingSetup;
import dk.via.group1.urbanmicrofarm_backend.database.entities.GrowingSetupEntity;
import org.springframework.stereotype.Component;

@Component
public class GrowingSetupPersistenceMapper {

  public GrowingSetupEntity toEntity(String email, GrowingSetup growingSetup) {
    GrowingSetupEntity entity = new GrowingSetupEntity();
    entity.setSerialNumber(growingSetup.getSerialNumber());
    entity.setLocation(growingSetup.getLocation());
    entity.setEmail(email);
    return entity;
  }

  public GrowingSetup toDomain(GrowingSetupEntity entity) {
    return new GrowingSetup(
        entity.getSetupId(),
        entity.getSerialNumber(),
        entity.getLocation(),
        null
    );
  }
}