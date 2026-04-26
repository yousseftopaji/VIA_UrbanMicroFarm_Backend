package dk.via.group1.urbanmicrofarm_backend.mqtt.publisher;

import dk.via.group1.urbanmicrofarm_backend.mqtt.client.MqttClientFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DefaultMqttPublisher implements MqttPublisher {

  private final MqttClientFactory clientFactory;

  public DefaultMqttPublisher(MqttClientFactory clientFactory) {
    this.clientFactory = clientFactory;
  }

  @Override
  public void publish(String topic, String payload) throws MqttException {
    MqttClient client = clientFactory.createPublisherClient();
    try {
      MqttMessage message = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
      message.setQos(1);
      client.publish(topic, message);
    } finally {
      if (client.isConnected()) {
        client.disconnect();
      }
      client.close();
    }
  }
}