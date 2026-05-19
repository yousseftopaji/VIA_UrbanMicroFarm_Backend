package dk.via.group1.urbanmicrofarm_backend.application.services.predictions;

import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionResponseDto;

public interface PredictionService {

    WaterPredictionResponseDto predictWater(
            WaterPredictionRequestDto request);
}