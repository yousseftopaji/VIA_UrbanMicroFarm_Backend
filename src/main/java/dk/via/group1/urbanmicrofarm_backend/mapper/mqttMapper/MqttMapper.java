package dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper;

public interface MqttMapper<T>
{

  T fromPayload(String payload);

  String toPayload(T source);
}