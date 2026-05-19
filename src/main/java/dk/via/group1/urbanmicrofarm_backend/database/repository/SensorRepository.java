package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<SensorEntity, Long> {

  List<SensorEntity> findBySerialNumber(String serialNumber);

  List<SensorEntity> findBySerialNumberAndSensorTypeName(String serialNumber, String sensorTypeName);
  
  java.util.Optional<SensorEntity> findFirstBySerialNumberAndSensorTypeName(String serialNumber, String sensorTypeName);
}