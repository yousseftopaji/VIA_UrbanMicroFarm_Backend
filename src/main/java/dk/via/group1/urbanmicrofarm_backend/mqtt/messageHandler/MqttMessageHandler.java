package dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler;

import org.springframework.stereotype.Service;

@Service
public interface MqttMessageHandler
{
  void handle(String topic, String payload);
}