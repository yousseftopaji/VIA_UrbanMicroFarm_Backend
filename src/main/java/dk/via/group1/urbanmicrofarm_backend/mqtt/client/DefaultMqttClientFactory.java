package dk.via.group1.urbanmicrofarm_backend.mqtt.client;

import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

@Component
public class DefaultMqttClientFactory implements MqttClientFactory {

  private final MqttClientConfig config;

  public DefaultMqttClientFactory(MqttClientConfig config) {
    this.config = config;
  }

  @Override
  public MqttClient createSubscriberClient() throws MqttException {
    MqttClient client = new MqttClient(requireBroker(), requireClientId());
    client.connect(subscriberOptions());
    return client;
  }

  @Override
  public MqttClient createPublisherClient() throws MqttException {
    // Use dedicated publisher id to avoid client-id collisions.
    MqttClient client = new MqttClient(requireBroker(), requireClientId() + "-publisher");
    client.connect(publisherOptions());
    return client;
  }

  private String requireBroker() {
    String broker = config.getBroker();
    if (broker == null || broker.isBlank()) {
      throw new IllegalArgumentException("broker must not be null or blank");
    }
    return broker;
  }

  private String requireClientId() {
    String clientId = config.getClientId();
    if (clientId == null || clientId.isBlank()) {
      throw new IllegalArgumentException("clientId must not be null or blank");
    }
    return clientId;
  }

  private MqttConnectOptions subscriberOptions() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(false); // needed for queued QoS1 delivery while offline
    options.setConnectionTimeout(10);
    return options;
  }

  private MqttConnectOptions publisherOptions() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    return options;
  }
}