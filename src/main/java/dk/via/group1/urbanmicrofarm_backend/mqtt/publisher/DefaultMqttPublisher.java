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
    if (topic == null || topic.isBlank()) {
      throw new IllegalArgumentException("topic must not be null or blank");
    }

    MqttClient client = clientFactory.createPublisherClient();
    try {
      MqttMessage message = new MqttMessage(payload == null
          ? new byte[0]
          : payload.getBytes(StandardCharsets.UTF_8));
      message.setQos(1);
      client.publish(topic, message);
    } finally {
      try {
        if (client.isConnected()) {
          client.disconnect();
        }
      } finally {
        client.close();
      }
    }
  }
}