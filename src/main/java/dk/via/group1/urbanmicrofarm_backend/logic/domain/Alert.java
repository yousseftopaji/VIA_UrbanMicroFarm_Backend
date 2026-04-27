package dk.via.group1.urbanmicrofarm_backend.logic.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alert", schema = "urban_micro_farm_app")
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "alert_type")
  private String type;

  private String message;
  private String status;
  private LocalDateTime timestamp;

  @ManyToOne
  @JoinColumn(name = "sensor_reading_id")
  private SensorReading sensorReading;

  @ManyToOne
  @JoinColumn(name = "watering_event_id")
  private WateringEvent wateringEvent;

  public Alert() {}

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public LocalDateTime getTimestamp() { return timestamp; }
  public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

  public SensorReading getSensorReading() { return sensorReading; }
  public void setSensorReading(SensorReading sensorReading) { this.sensorReading = sensorReading; }

  public WateringEvent getWateringEvent() { return wateringEvent; }
  public void setWateringEvent(WateringEvent wateringEvent) { this.wateringEvent = wateringEvent; }
}