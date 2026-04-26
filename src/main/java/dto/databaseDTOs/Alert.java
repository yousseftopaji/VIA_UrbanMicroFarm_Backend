package dto.databaseDTOs;

import java.time.LocalDateTime;

public class Alert {
  private final int id;
  private final String type;
  private final String message;
  private final String status;
  private final LocalDateTime timestamp;
  private final int sensorReadingId;
  private final int wateringEventId;

  public Alert(int id, String type, String message, String status, LocalDateTime timestamp, int sensorReadingId, int wateringEventId) {
    this.id = id;
    this.type = type;
    this.message = message;
    this.status = status;
    this.timestamp = timestamp;
    this.sensorReadingId = sensorReadingId;
    this.wateringEventId = wateringEventId;
  }

  public int getId() { return id; }
  public String getType() { return type; }
  public String getMessage() { return message; }
  public String getStatus() { return status; }
  public LocalDateTime getTimestamp() { return timestamp; }
  public int getSensorReadingId() { return sensorReadingId; }
  public int getWateringEventId() { return wateringEventId; }
}