package dk.via.group1.urbanmicrofarm_backend.mqtt.client;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttClientFactory {
  MqttClient createClient() throws MqttException;
}