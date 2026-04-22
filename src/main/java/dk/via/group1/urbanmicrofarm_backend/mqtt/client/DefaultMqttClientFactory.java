package dk.via.group1.urbanmicrofarm_backend.mqtt.client;

import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

@Component
public class DefaultMqttClientFactory implements MqttClientFactory
{
  private final MqttClientConfig mqttClientConfig;

  public DefaultMqttClientFactory(MqttClientConfig mqttClientConfig)
  {
    this.mqttClientConfig = mqttClientConfig;
  }

  @Override
  public MqttClient createClient() throws MqttException {
    return new MqttClient(
        mqttClientConfig.getBroker(),
        mqttClientConfig.getClientId(),
        new MemoryPersistence()
    );
  }
}