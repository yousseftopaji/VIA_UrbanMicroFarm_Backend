package dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler;

import dk.via.group1.urbanmicrofarm_backend.dtos.mqttDtos.MqttSensorReadingDto;
import dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper.MqttSensorReadingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SensorReadingMqttMessageHandler implements MqttMessageHandler {

  private static final Logger logger = LoggerFactory.getLogger(SensorReadingMqttMessageHandler.class);

  private final MqttSensorReadingMapper sensorReadingMapper;

  public SensorReadingMqttMessageHandler(MqttSensorReadingMapper sensorReadingMapper) {
    this.sensorReadingMapper = sensorReadingMapper;
  }

  @Override
  public void handle(String topic, String payload) {
    MqttSensorReadingDto reading = sensorReadingMapper.fromPayload(payload);

    // Next step: call your application service here with `reading`.
    logger.info("MQTT sensor reading received on topic {}: {}", topic, reading);
  }
}