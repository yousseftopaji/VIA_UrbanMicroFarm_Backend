package dk.via.group1.urbanmicrofarm_backend.sensorreading.repository;

import dk.via.group1.urbanmicrofarm_backend.sensorreading.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long>
{
  Optional<SensorReading> findFirstBySensorIdOrderByTimestampDesc(String sensorId); // avoids returning the null directly if no reading exists

  List<SensorReading> findBySensorIdOrderByTimestampDesc(String sensorId);
}
