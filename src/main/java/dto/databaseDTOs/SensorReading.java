package dto.databaseDTOs;

import java.time.LocalDateTime;

public class SensorReading {
  private final int id;
  private final int sensorId;
  private final double value;
  private final LocalDateTime timestamp;

  public SensorReading(int id, int sensorId, double value, LocalDateTime timestamp) {
    this.id = id;
    this.sensorId = sensorId;
    this.value = value;
    this.timestamp = timestamp;
  }

  public int getId() { return id; }
  public int getSensorId() { return sensorId; }
  public double getValue() { return value; }
  public LocalDateTime getTimestamp() { return timestamp; }
}