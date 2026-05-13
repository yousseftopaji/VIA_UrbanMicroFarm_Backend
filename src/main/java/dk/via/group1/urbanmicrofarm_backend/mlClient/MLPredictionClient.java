package dk.via.group1.urbanmicrofarm_backend.mlClient;

import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionResponseDto;

public interface MLPredictionClient {
    WaterPredictionResponseDto predictWater(WaterPredictionRequestDto request);
}
