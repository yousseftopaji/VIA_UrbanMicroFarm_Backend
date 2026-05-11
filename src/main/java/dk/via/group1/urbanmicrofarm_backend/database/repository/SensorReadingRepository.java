package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReadingEntity, Long> {


    Optional<SensorReadingEntity> findFirstBySensorIdOrderByTimestampDesc(Integer sensorId);

//    Custom query, to handle null Instant type parameters: "from" and "to"
    @Query("SELECT r FROM SensorReadingEntity r WHERE r.sensorId = :sensorId " +
            "AND (:from IS NULL OR r.timestamp >= :from) " +
            "AND (:to IS NULL OR r.timestamp <= :to) " +
            "ORDER BY r.timestamp DESC")

    List<SensorReadingEntity> findBySensorIdAndTimeRange(
            @Param("sensorId") Integer sensorId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );
}