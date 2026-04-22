package dk.via.group1.urbanmicrofarm_backend.mqtt.publisher;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttPublisher {

  void publish(String topic, String payload) throws MqttException;
}