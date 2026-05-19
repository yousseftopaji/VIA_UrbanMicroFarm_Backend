package dk.via.group1.urbanmicrofarm_backend.application.services.predictions;

import dk.via.group1.urbanmicrofarm_backend.application.domain.Prediction;
import dk.via.group1.urbanmicrofarm_backend.database.entities.PredictionEntity;
import dk.via.group1.urbanmicrofarm_backend.database.repository.PredictionRepository;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionResponseDto;
import dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper.PredictionPersistenceMapper;
import dk.via.group1.urbanmicrofarm_backend.mlClient.MLPredictionClient;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PredictionServiceImpl
        implements PredictionService {

    private final MLPredictionClient mlClient;
    private final PredictionRepository predictionRepository;
    private final PredictionPersistenceMapper predictionMapper;

    public PredictionServiceImpl(
            MLPredictionClient mlClient,
            PredictionRepository predictionRepository,
            PredictionPersistenceMapper predictionMapper) {

        this.mlClient = mlClient;
        this.predictionRepository = predictionRepository;
        this.predictionMapper = predictionMapper;
    }

    @Override
    public WaterPredictionResponseDto predictWater(
            WaterPredictionRequestDto request) {

        // Call ML service
        WaterPredictionResponseDto response =
                mlClient.predictWater(request);

        // Create domain object
        Prediction prediction = new Prediction();

        prediction.setPredictedValue(
                (double) response.wateringAmount());

        prediction.setPlantName("Plant 1");

        prediction.setCreatedAt(Instant.now());

        // Convert to entity
        PredictionEntity entity =
                predictionMapper.toEntity(prediction);

        // Save to database
        predictionRepository.save(entity);

        return response;
    }
}