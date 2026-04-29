package dk.via.group1.urbanmicrofarm_backend.mqtt.subscriber;

import dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler.MqttMessageHandler;
import dk.via.group1.urbanmicrofarm_backend.mqtt.client.MqttClientFactory;
import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DefaultMqttSubscriber implements MqttSubscriber, ApplicationRunner {

  private final MqttClientConfig config;
  private final MqttClientFactory clientFactory;
  private final MqttMessageHandler messageHandler;

  private MqttClient client;

  public DefaultMqttSubscriber(
      MqttClientConfig config,
      MqttClientFactory clientFactory,
      MqttMessageHandler messageHandler
  ) {
    this.config = config;
    this.clientFactory = clientFactory;
    this.messageHandler = messageHandler;
  }
  @Override
  public void start() throws MqttException {
    client = clientFactory.createSubscriberClient();
    subscribe(config.getTopic());
  }

  @Override
  public void subscribe(String topic) throws MqttException {
    if (client == null || !client.isConnected()) {
      throw new IllegalStateException("MQTT subscriber client is not connected");
    }

    client.subscribe(topic, 1, (receivedTopic, message) -> {
      String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
      messageHandler.handle(receivedTopic, payload);
    });
  }

  @Override
  public void stop() throws MqttException {
    if (client != null) {
      try {
        if (client.isConnected()) {
          client.disconnect();
        }
      } finally {
        client.close();
      }
    }
  }

  @Override
  public void run(@Nullable ApplicationArguments args) throws Exception {
    start();
  }
}