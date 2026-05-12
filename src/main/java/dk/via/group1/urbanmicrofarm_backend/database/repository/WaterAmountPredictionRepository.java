package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.WaterAmountPredictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterAmountPredictionRepository
        extends JpaRepository<WaterAmountPredictionEntity, Long> {
}