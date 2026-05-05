package dk.via.group1.urbanmicrofarm_backend.dto.mqttDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ActuatorCommandDto(
        String actuator,

        @JsonProperty("amounl_ml")
        int amountMl
) {}