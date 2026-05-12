package dk.via.group1.urbanmicrofarm_backend.application.services.predictions;

import dk.via.group1.urbanmicrofarm_backend.database.entities.WaterAmountPredictionEntity;
import dk.via.group1.urbanmicrofarm_backend.database.repository.WaterAmountPredictionRepository;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionResponseDto;
import dk.via.group1.urbanmicrofarm_backend.mlClient.MLPredictionClient;
import org.springframework.stereotype.Service;

@Service
public class WaterPredictionServiceImpl
        implements WaterPredictionService {

    private final MLPredictionClient mlClient;

    private final WaterAmountPredictionRepository
            waterPredictionRepository;

    public WaterPredictionServiceImpl(
            MLPredictionClient mlClient,
            WaterAmountPredictionRepository
                    waterPredictionRepository) {

        this.mlClient = mlClient;
        this.waterPredictionRepository =
                waterPredictionRepository;
    }

    @Override
    public WaterPredictionResponseDto predictWater(
            WaterPredictionRequestDto request) {

        // Call ML API
        WaterPredictionResponseDto response =
                mlClient.predictWater(request);

        // Create entity
        WaterAmountPredictionEntity entity =
                new WaterAmountPredictionEntity();

        // Save ML response
        entity.setWateringAmount(
                response.wateringAmount());

        // Save to database
        waterPredictionRepository.save(entity);

        return response;
    }
}