package dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.MqttTelemetryDataDto;
import dk.via.group1.urbanmicrofarm_backend.logic.services.SensorReadingService;
import dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper.MqttSensorReadingMapper;
import dk.via.group1.urbanmicrofarm_backend.mqtt.parser.MqttTelemetryDataParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SensorReadingMqttMessageHandler implements MqttMessageHandler {

  private static final Logger logger = LoggerFactory.getLogger(SensorReadingMqttMessageHandler.class);

  private final MqttTelemetryDataParser telemetryDataParser;
  private final MqttSensorReadingMapper sensorReadingMapper;
  private final SensorReadingService sensorReadingService;

  public SensorReadingMqttMessageHandler(
      MqttTelemetryDataParser telemetryDataParser,
      MqttSensorReadingMapper sensorReadingMapper,
      SensorReadingService sensorReadingService
  ) {
    this.telemetryDataParser = telemetryDataParser;
    this.sensorReadingMapper = sensorReadingMapper;
    this.sensorReadingService = sensorReadingService;
  }

  @Override
  public void handle(String topic, String payload) {

    TelemetryData reading;
    try
    {
      MqttTelemetryDataDto readingDto = telemetryDataParser.fromJson(payload);
      if (readingDto == null) {
        logger.error("Failed to parse MQTT message on topic {}: parser returned null", topic);
        return;
      }
      reading = sensorReadingMapper.fromPayload(readingDto);
      logger.info("MQTT sensor reading received on topic {}: {}", topic, reading);
      sensorReadingService.processReadings(reading);
    }
    catch (Exception e)
    {
      logger.error("Failed to parse MQTT message on topic {}: {}", topic, e.getMessage());
    }
  }
}