package dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.MqttTelemetryDataDto;
import org.springframework.stereotype.Component;

@Component
public class MqttSensorReadingMapper implements MqttMapper<MqttTelemetryDataDto, TelemetryData> {

  @Override
  public TelemetryData fromPayload(MqttTelemetryDataDto payload) {
    if (payload == null) {
      throw new IllegalArgumentException("Telemetry payload must not be null");
    }

    if (payload.setupId() <= 0) {
      throw new IllegalArgumentException(
          "Invalid telemetry data: setupId must be greater than 0"
      );
    }
    if ( payload.sensorId() == null) {
      throw new IllegalArgumentException(
          "Invalid telemetry data: sensorId must not be null"
      );
    }

    return new TelemetryData(
        payload.setupId(),
        payload.sensorId(),
        payload.temperature(),
        payload.humidity(),
        payload.light(),
        payload.soilMoisture()
    );
  }

  @Override
  public MqttTelemetryDataDto toPayload(TelemetryData source) {
    if (source == null) {
      throw new IllegalArgumentException("Telemetry domain object must not be null");
    }

    return new MqttTelemetryDataDto(
        source.setupId(),
        source.sensorId(),
        source.temperature(),
        source.humidity(),
        source.light(),
        source.soilMoisture()
    );
  }
}