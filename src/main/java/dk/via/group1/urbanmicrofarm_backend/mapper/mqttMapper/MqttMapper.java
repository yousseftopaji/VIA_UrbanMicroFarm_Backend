package dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper;

public interface MqttMapper<DTO, Domain> {
  Domain fromPayload(DTO payload);
  DTO toPayload(Domain source);
}