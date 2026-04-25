package dk.via.group1.urbanmicrofarm_backend.aplication.database.repository;

import dk.via.group1.urbanmicrofarm_backend.aplication.database.entities.SensorReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReadingEntity, Long> {

    Optional<SensorReadingEntity> findFirstBySetupIdAndSensorTypeOrderByTimestampDesc(
            Integer setupId,
            String sensorType
    );

    List<SensorReadingEntity> findBySetupIdAndSensorTypeOrderByTimestampDesc(
            Integer setupId,
            String sensorType
    );
}