package dk.via.group1.urbanmicrofarm_backend.dto.mlDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WaterPredictionResponseDto(
        @JsonProperty("watering_amount")
        int wateringAmount
) {}
