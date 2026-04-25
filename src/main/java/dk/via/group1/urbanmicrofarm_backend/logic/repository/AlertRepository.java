package dk.via.group1.urbanmicrofarm_backend.logic.repository;

import dk.via.group1.urbanmicrofarm_backend.logic.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {
}