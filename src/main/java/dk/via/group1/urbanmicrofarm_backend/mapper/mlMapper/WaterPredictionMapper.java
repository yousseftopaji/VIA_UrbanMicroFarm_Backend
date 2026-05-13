package dk.via.group1.urbanmicrofarm_backend.mapper.mlMapper;

import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;

public interface WaterPredictionMapper {
    WaterPredictionRequestDto toRequestDto(
            double temperature,
            double humidity,
            int light,
            double soilMoisture
    );
}
