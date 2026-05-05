package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.GrowingSetupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrowingSetupRepository extends JpaRepository<GrowingSetupEntity, Long> {

  List<GrowingSetupEntity> findByEmail(String email);
}