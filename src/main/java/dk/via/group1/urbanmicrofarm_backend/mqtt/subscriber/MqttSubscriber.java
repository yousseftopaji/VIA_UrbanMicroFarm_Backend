package dk.via.group1.urbanmicrofarm_backend.mqtt.subscriber;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttSubscriber {

  void start() throws MqttException;

  void stop() throws MqttException;

  void subscribe(String topic) throws MqttException;
}