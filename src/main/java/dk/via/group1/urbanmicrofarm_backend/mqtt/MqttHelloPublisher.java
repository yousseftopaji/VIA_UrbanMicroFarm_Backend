package dk.via.group1.urbanmicrofarm_backend.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class MqttHelloPublisher implements ApplicationRunner {

  @Value("${mqtt.broker:tcp://20.240.208.122:1883}")
  private String broker;

  @Value("${mqtt.publisher.client-id:backend-hello-publisher}")
  private String clientId;

  @Value("${mqtt.topic:farm/test/hello}")
  private String topic;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());

    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(false);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);

    client.connect(options);

    String payload = "{\"message\":\"hello from backend, this is Youssef : trial 2\"}";
    MqttMessage message = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
    message.setQos(1);

    client.publish(topic, message);
    client.disconnect();
    client.close();

    System.out.println("MQTT hello sent to topic: " + topic + "tial 1");
  }
}