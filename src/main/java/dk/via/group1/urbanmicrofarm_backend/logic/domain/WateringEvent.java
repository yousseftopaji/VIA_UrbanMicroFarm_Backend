package dk.via.group1.urbanmicrofarm_backend.logic.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "watering_event", schema = "urban_micro_farm_app")
public class WateringEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private LocalDateTime startTime;
  private LocalDateTime endTime;

  @Column(name = "water_used_liters")
  private Double waterUsedLiters;

  private String mode;

  @ManyToOne
  @JoinColumn(name = "actuator_id", nullable = false)
  private Actuator actuator;

  @OneToMany(mappedBy = "wateringEvent", cascade = CascadeType.ALL)
  private List<Alert> alerts;

  public WateringEvent() {}

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public LocalDateTime getStartTime() { return startTime; }
  public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

  public LocalDateTime getEndTime() { return endTime; }
  public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

  public Double getWaterUsedLiters() { return waterUsedLiters; }
  public void setWaterUsedLiters(Double waterUsedLiters) { this.waterUsedLiters = waterUsedLiters; }

  public String getMode() { return mode; }
  public void setMode(String mode) { this.mode = mode; }

  public Actuator getActuator() { return actuator; }
  public void setActuator(Actuator actuator) { this.actuator = actuator; }

  public List<Alert> getAlerts() { return alerts; }
  public void setAlerts(List<Alert> alerts) { this.alerts = alerts; }
}