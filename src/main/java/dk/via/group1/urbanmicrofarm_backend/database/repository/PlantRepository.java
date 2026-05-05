package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<PlantEntity, String> {

  List<PlantEntity> findBySetupId(Long setupId);
}