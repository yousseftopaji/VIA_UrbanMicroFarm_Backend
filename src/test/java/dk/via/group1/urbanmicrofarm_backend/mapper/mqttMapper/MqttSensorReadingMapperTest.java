package dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper;

import dk.via.group1.urbanmicrofarm_backend.dtos.mqttDtos.MqttSensorReadingDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class MqttSensorReadingMapperTest {

  @Test
  void fromPayload_zombiesZeroOneMany_currentImplementationReturnsNull() {
    MqttMapper<MqttSensorReadingDto> mapper = new MqttSensorReadingMapper();

    assertNull(mapper.fromPayload(""));
    assertNull(mapper.fromPayload("{\"value\":10}"));
    assertNull(mapper.fromPayload("[{\"value\":1},{\"value\":2}]"));
  }

  @Test
  void toPayload_boundaryNullSource_currentImplementationReturnsNull() {
    MqttMapper<MqttSensorReadingDto> mapper = new MqttSensorReadingMapper();

    assertNull(mapper.toPayload(null));
  }
}

