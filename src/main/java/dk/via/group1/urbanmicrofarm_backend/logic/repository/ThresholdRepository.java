package dk.via.group1.urbanmicrofarm_backend.logic.repository;

import dk.via.group1.urbanmicrofarm_backend.logic.domain.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThresholdRepository extends JpaRepository<Threshold, Integer> {
}
