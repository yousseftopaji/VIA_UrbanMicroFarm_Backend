package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "watering_event", schema = "urban_micro_farm_app")
public class WateringEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long wateringEventId;

  @Column(nullable = false)
  private Instant startTime;

  private Instant endTime;
  private Double waterUsedLiters;
  private String mode;

  @Column(nullable = false)
  private String actuatorType;

  public Long getWateringEventId() { return wateringEventId; }
  public void setWateringEventId(Long wateringEventId) { this.wateringEventId = wateringEventId; }

  public Instant getStartTime() { return startTime; }
  public void setStartTime(Instant startTime) { this.startTime = startTime; }

  public Instant getEndTime() { return endTime; }
  public void setEndTime(Instant endTime) { this.endTime = endTime; }

  public Double getWaterUsedLiters() { return waterUsedLiters; }
  public void setWaterUsedLiters(Double waterUsedLiters) { this.waterUsedLiters = waterUsedLiters; }

  public String getMode() { return mode; }
  public void setMode(String mode) { this.mode = mode; }

  public String getActuatorType() { return actuatorType; }
  public void setActuatorType(String actuatorType) { this.actuatorType = actuatorType; }
}