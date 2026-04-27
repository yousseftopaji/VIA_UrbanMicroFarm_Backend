package dk.via.group1.urbanmicrofarm_backend.logic.repository;

import dk.via.group1.urbanmicrofarm_backend.logic.domain.Actuator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, Integer> {
}