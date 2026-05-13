package dk.via.group1.urbanmicrofarm_backend.mapper.mlMapper;

import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;
import org.springframework.stereotype.Component;

@Component
public class WaterPredictionMapperImpl implements WaterPredictionMapper {

    @Override
    public WaterPredictionRequestDto toRequestDto(
            double temperature,
            double humidity,
            int light,
            double soilMoisture) {

        return new WaterPredictionRequestDto(
                temperature,
                humidity,
                light,
                soilMoisture
        );
    }
}
