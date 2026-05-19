package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.WateringEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WateringEventRepository extends JpaRepository<WateringEventEntity, Long> {

  List<WateringEventEntity> findByActuatorId(Long actuatorId);

  Optional<WateringEventEntity> findFirstByActuatorIdOrderByIdDesc(Long actuatorId);
}
