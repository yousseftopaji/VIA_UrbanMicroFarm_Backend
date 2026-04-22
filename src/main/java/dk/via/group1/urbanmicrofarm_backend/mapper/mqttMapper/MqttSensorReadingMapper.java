package dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper;

import dk.via.group1.urbanmicrofarm_backend.dtos.mqttDtos.MqttSensorReadingDto;
import org.springframework.stereotype.Component;

@Component
public class MqttSensorReadingMapper implements MqttMapper<MqttSensorReadingDto> {

  @Override
  public MqttSensorReadingDto fromPayload(String payload) {
    return null;
  }

  @Override
  public String toPayload(MqttSensorReadingDto source) {
    return null;
  }
}