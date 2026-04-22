package dk.via.group1.urbanmicrofarm_backend.mqtt.client;

import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultMqttClientFactoryTest {

  @Test
  void createClient_oneValidConfig_returnsClientWithConfiguredValues() throws MqttException {
    MqttClientConfig config = new MqttClientConfig();
    config.setBroker("tcp://localhost:1883");
    config.setClientId("zombies-client-1");

    MqttClientFactory factory = new DefaultMqttClientFactory(config);

    MqttClient client = factory.createClient();
    try {
      assertEquals("tcp://localhost:1883", client.getServerURI());
      assertEquals("zombies-client-1", client.getClientId());
    } finally {
      client.close();
    }
  }

  @Test
  void createClient_boundaryNullBroker_throwsNullPointerException() {
    MqttClientConfig config = new MqttClientConfig();
    config.setBroker(null);
    config.setClientId("zombies-client-2");

    MqttClientFactory factory = new DefaultMqttClientFactory(config);

    assertThrows(NullPointerException.class, factory::createClient);
  }
}

