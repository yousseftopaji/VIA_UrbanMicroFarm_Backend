package dk.via.group1.urbanmicrofarm_backend.application.services.plant_service;

import dk.via.group1.urbanmicrofarm_backend.database.entities.PlantEntity;
import dk.via.group1.urbanmicrofarm_backend.database.repository.PlantRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;

    public PlantServiceImpl(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public List<PlantEntity> getPlantsBySetup(Long setupId) {
        return plantRepository.findBySetupId(setupId);
    }

    @Override
    public Optional<PlantEntity> getPlant(Long plantId) {
        return plantRepository.findById(plantId);
    }

    @Override
    public Optional<PlantEntity> getPlantBySensor(Long sensorId) {
        return plantRepository.findBySensorId(sensorId);
    }

    @Override
    public PlantEntity addPlant(PlantEntity plantEntity) {

        plantEntity.setDatePlanted(Instant.now());

        if (plantEntity.getStatus() == null) {
            plantEntity.setStatus("growing");
        }

        return plantRepository.save(plantEntity);
    }

    @Override
    public PlantEntity updatePlant(Long plantId, PlantEntity updatedPlant) {

        PlantEntity existingPlant = plantRepository.findById(plantId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));

        if (updatedPlant.getName() != null) {
            existingPlant.setName(updatedPlant.getName());
        }

        if (updatedPlant.getDescription() != null) {
            existingPlant.setDescription(updatedPlant.getDescription());
        }

        return plantRepository.save(existingPlant);
    }

    @Override
    public void removePlant(Long plantId) {
        plantRepository.deleteById(plantId);
    }
}