package dk.via.group1.urbanmicrofarm_backend.dto.mqttDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MqttTelemetryDataDto(int setupId, Integer sensorId, int temperature,
                                   int humidity, int light, int soilMoisture)
{
  @JsonCreator public MqttTelemetryDataDto(
      @JsonProperty("setup_id") int setupId,
      @JsonProperty("sensor_id") Integer sensorId,
      @JsonProperty("temperature") int temperature,
      @JsonProperty("humidity") int humidity, @JsonProperty("light") int light,
      @JsonProperty("soil_moisture") int soilMoisture)
  {
    this.setupId = setupId;
    this.sensorId = sensorId;
    this.temperature = temperature;
    this.humidity = humidity;
    this.light = light;
    this.soilMoisture = soilMoisture;
  }
}