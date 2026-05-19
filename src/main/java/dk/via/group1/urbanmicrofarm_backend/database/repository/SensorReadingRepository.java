package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReadingEntity, Long> {
  Optional<SensorReadingEntity> findFirstBySensorIdOrderByTimestampDesc(Long sensorId);
  List<SensorReadingEntity> findBySensorIdOrderByTimestampDesc(Long sensorId);
}