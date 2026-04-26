package dto.databaseDTOs;

public class Sensor {
  private final int sensorId;
  private final String type;
  private final String unit;
  private final int setupId;

  public Sensor(int sensorId, String type, String unit, int setupId) {
    this.sensorId = sensorId;
    this.type = type;
    this.unit = unit;
    this.setupId = setupId;
  }

  public int getSensorId() { return sensorId; }
  public String getType() { return type; }
  public String getUnit() { return unit; }
  public int getSetupId() { return setupId; }
}