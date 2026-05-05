package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.ThresholdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThresholdRepository extends JpaRepository<ThresholdEntity, String> {

  Optional<ThresholdEntity> findByType(String type);
}