package dk.via.group1.urbanmicrofarm_backend.dtos.mqttDtos;

public class MqttSensorDto
{
  private long sensorId;

  public MqttSensorDto(long sensorId)
  {
    this.sensorId = sensorId;
  }

  public long getSensorId()
  {
    return sensorId;
  }
}
