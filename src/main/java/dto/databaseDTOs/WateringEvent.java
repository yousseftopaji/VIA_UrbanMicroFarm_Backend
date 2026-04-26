package dto.databaseDTOs;

import java.time.LocalDateTime;

public class WateringEvent {
  private final int id;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final double waterUsedLiters;
  private final String mode;
  private final int actuatorId;

  public WateringEvent(int id, LocalDateTime startTime, LocalDateTime endTime, double waterUsedLiters, String mode, int actuatorId) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.waterUsedLiters = waterUsedLiters;
    this.mode = mode;
    this.actuatorId = actuatorId;
  }

  public int getId() { return id; }
  public LocalDateTime getStartTime() { return startTime; }
  public LocalDateTime getEndTime() { return endTime; }
  public double getWaterUsedLiters() { return waterUsedLiters; }
  public String getMode() { return mode; }
  public int getActuatorId() { return actuatorId; }
}