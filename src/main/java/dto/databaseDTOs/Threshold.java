package dto.databaseDTOs;

import java.time.LocalDateTime;

public class Threshold {
  private final int id;
  private final String type;
  private final double value;
  private final LocalDateTime createdAt;
  private final int sensorReadingId;

  public Threshold(int id, String type, double value, LocalDateTime createdAt, int sensorReadingId) {
    this.id = id;
    this.type = type;
    this.value = value;
    this.createdAt = createdAt;
    this.sensorReadingId = sensorReadingId;
  }

  public int getId() { return id; }
  public String getType() { return type; }
  public double getValue() { return value; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public int getSensorReadingId() { return sensorReadingId; }
}