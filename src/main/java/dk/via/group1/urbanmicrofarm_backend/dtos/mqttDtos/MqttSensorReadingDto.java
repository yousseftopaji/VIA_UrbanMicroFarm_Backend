package dk.via.group1.urbanmicrofarm_backend.dtos.mqttDtos;

import java.security.Timestamp;

public class MqttSensorReadingDto
{
  public int value;
  public Timestamp timestamp;

  public MqttSensorReadingDto(int value, Timestamp timestamp)
  {
    this.value = value;
  }

  public int getValue()
  {
    return value;
  }

  public Timestamp getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp)
  {
    this.timestamp = timestamp;
  }

  public void setValue(int value)
  {
    this.value = value;
  }
}
