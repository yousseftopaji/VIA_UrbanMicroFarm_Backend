package dk.via.group1.urbanmicrofarm_backend.mqtt.parser;

import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.MqttTelemetryDataDto;

public interface MqttTelemetryDataParser
{
  MqttTelemetryDataDto fromJson(String payload);
  String toJson(MqttTelemetryDataDto payload);
}
