package dk.via.group1.urbanmicrofarm_backend.mqtt.parser;

import tools.jackson.databind.ObjectMapper;
import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.MqttTelemetryDataDto;
import org.springframework.stereotype.Component;

@Component
public class MqttTelemetryDataParserImpl implements MqttTelemetryDataParser
{
  private final ObjectMapper objectMapper;

  public MqttTelemetryDataParserImpl(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }
  @Override
  public MqttTelemetryDataDto fromJson(String payload) {
    if (payload == null || payload.isBlank()) {
      throw new IllegalArgumentException("MQTT payload must not be blank");
    }

    try {
      return objectMapper.readValue(payload, MqttTelemetryDataDto.class);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid MQTT telemetry JSON payload", e);
    }
  }

  @Override
  public String toJson(MqttTelemetryDataDto payload) {
    if (payload == null) {
      throw new IllegalArgumentException("MQTT telemetry DTO must not be null");
    }

    try {
      return objectMapper.writeValueAsString(payload);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to serialize MQTT telemetry DTO", e);
    }
  }
}

