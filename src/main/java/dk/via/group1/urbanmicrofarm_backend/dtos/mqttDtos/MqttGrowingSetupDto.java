package dk.via.group1.urbanmicrofarm_backend.dtos.mqttDtos;

public class MqttGrowingSetupDto
{
  private long serialNumber;

  public MqttGrowingSetupDto()
  {
  }

  public long getSerialNumber()
  {
    return serialNumber;
  }

  public void setSerialNumber(long serialNumber)
  {
    this.serialNumber = serialNumber;
  }
}
