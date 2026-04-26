package dk.via.group1.urbanmicrofarm_backend.mqtt.parser;

import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.MqttTelemetryDataDto;

public interface MqttTelemetryDataParser
{
  public MqttTelemetryDataDto fromJson(String payload);
  public String toJson(MqttTelemetryDataDto payload);
}
