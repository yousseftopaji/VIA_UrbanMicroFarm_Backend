package dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler;

import dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper.MqttSensorReadingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SensorReadingMqttMessageHandlerTest {

  @Mock
  private MqttSensorReadingMapper mapper;

  @Test
  void handle_onePayload_mapsPayload() {
    MqttMessageHandler handler = new SensorReadingMqttMessageHandler(mapper);

    handler.handle("farm/test/handler", "{\"value\":21}");

    verify(mapper).fromPayload("{\"value\":21}");
  }

  @Test
  void handle_exceptionFromMapper_propagatesException() {
    MqttMessageHandler handler = new SensorReadingMqttMessageHandler(mapper);
    doThrow(new IllegalArgumentException("bad payload")).when(mapper).fromPayload("bad");

    assertThrows(IllegalArgumentException.class, () -> handler.handle("farm/test/handler", "bad"));
  }
}

