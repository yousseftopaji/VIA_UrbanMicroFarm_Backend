package dk.via.group1.urbanmicrofarm_backend.mqtt.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MqttClientConfigTest {

  @Test
  void gettersAndSetters_oneValues_returnConfiguredValues() {
    MqttClientConfig config = new MqttClientConfig();

    config.setBroker("tcp://localhost:1883");
    config.setClientId("zombies-config-client");
    config.setTopic("farm/test/config");

    assertEquals("tcp://localhost:1883", config.getBroker());
    assertEquals("zombies-config-client", config.getClientId());
    assertEquals("farm/test/config", config.getTopic());
  }

  @Test
  void gettersAndSetters_boundaryNullValues_allowNull() {
    MqttClientConfig config = new MqttClientConfig();

    config.setBroker(null);
    config.setClientId(null);
    config.setTopic(null);

    assertNull(config.getBroker());
    assertNull(config.getClientId());
    assertNull(config.getTopic());
  }
}

