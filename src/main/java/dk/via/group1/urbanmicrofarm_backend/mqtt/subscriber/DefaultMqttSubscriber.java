package dk.via.group1.urbanmicrofarm_backend.mqtt.subscriber;

import dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler.MqttMessageHandler;
import dk.via.group1.urbanmicrofarm_backend.mqtt.client.MqttClientFactory;
import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DefaultMqttSubscriber implements MqttSubscriber, ApplicationRunner {

  private final MqttClientConfig config;
  private final MqttClientFactory clientFactory;
  private MqttMessageHandler messageHandler;

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
    client = clientFactory.createClient();
    client.connect();
    subscribe(config.getTopic());
  }

  @Override
  public void stop() throws MqttException {
    if (client != null)
    {if(client.isConnected())
    {
      client.disconnect();
    }
    client.close();}
  }

  @Override
  public void subscribe(String topic) throws MqttException {
    if (client == null) {
      throw new IllegalStateException("MQTT client is not started");
    }

    client.subscribe(topic, (IMqttMessageListener)
        (receivedTopic, message) -> {
      String payload = new String(message.getPayload(),
          StandardCharsets.UTF_8);
          System.out.println("Received MQTT message on topic " + receivedTopic + ": " + payload);
      messageHandler.handle(receivedTopic, payload);
    });
  }
  @Override
  public void run(ApplicationArguments args) throws Exception {
    start();
  }
}