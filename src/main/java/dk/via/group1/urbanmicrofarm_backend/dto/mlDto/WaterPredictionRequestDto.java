package dk.via.group1.urbanmicrofarm_backend.dto.mlDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WaterPredictionRequestDto(
        double temperature,
        double humidity,
        int light,

        @JsonProperty("soil_moisture")
        double soilMoisture
) {}
