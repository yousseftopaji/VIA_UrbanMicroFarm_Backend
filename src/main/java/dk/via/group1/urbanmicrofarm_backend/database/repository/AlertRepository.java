package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {

  List<AlertEntity> findBySensorReadingIdOrderByTimestampDesc(Long sensorReadingId);

  List<AlertEntity> findByWateringEventIdOrderByTimestampDesc(Long wateringEventId);

  List<AlertEntity> findByStatusOrderByTimestampDesc(String status);
}