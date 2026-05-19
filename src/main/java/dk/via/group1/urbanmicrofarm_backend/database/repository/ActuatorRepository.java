package dk.via.group1.urbanmicrofarm_backend.database.repository;

import dk.via.group1.urbanmicrofarm_backend.database.entities.ActuatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActuatorRepository extends JpaRepository<ActuatorEntity, Long> {

  List<ActuatorEntity> findBySerialNumber(String serialNumber);
}