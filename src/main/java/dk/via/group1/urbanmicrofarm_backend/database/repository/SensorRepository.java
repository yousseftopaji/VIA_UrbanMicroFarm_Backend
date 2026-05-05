package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<SensorEntity, Long> {

  List<SensorEntity> findBySetupId(Long setupId);

  List<SensorEntity> findBySetupIdAndSensorTypeName(Long setupId, String sensorTypeName);
}