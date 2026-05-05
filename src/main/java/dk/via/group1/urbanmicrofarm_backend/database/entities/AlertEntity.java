package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "alert", schema = "urban_micro_farm_app")
public class AlertEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long alertId;

  @Column(nullable = false)
  private String type;

  private String message;
  private String status;

  @Column(nullable = false)
  private Instant timestamp;

  private Long sensorReadingId;
  private Instant wateringEventStartTime;

  public Long getAlertId() { return alertId; }
  public void setAlertId(Long alertId) { this.alertId = alertId; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public Instant getTimestamp() { return timestamp; }
  public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

  public Long getSensorReadingId() { return sensorReadingId; }
  public void setSensorReadingId(Long sensorReadingId) { this.sensorReadingId = sensorReadingId; }

  public Instant getWateringEventStartTime() { return wateringEventStartTime; }
  public void setWateringEventStartTime(Instant wateringEventStartTime) { this.wateringEventStartTime = wateringEventStartTime; }
}