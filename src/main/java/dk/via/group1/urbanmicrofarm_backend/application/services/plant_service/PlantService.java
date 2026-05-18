package dk.via.group1.urbanmicrofarm_backend.application.services.plant_service;

import dk.via.group1.urbanmicrofarm_backend.database.entities.PlantEntity;

import java.util.List;
import java.util.Optional;

public interface PlantService {

    List<PlantEntity> getPlantsBySetup(Long setupId);

    Optional<PlantEntity> getPlant(Long plantId);

    Optional<PlantEntity> getPlantBySensor(Long sensorId);

    PlantEntity addPlant(PlantEntity plantEntity);

    PlantEntity updatePlant(Long plantId, PlantEntity updatedPlant);

    void removePlant(Long plantId);
}