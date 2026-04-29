package dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler;
public interface MqttMessageHandler
{
  void handle(String topic, String payload);
}