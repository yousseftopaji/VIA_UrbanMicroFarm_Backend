package dk.via.group1.urbanmicrofarm_backend.logic.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "threshold", schema = "urban_micro_farm_app")
public class Threshold {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "threshold_type", nullable = false)
  private String type;

  @Column(name = "threshold_value")
  private Double value;

  private LocalDateTime createdAt;

  @OneToOne
  @JoinColumn(name = "sensor_reading_id")
  private SensorReading sensorReading;

  public Threshold() {}

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public Double getValue() { return value; }
  public void setValue(Double value) { this.value = value; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

  public SensorReading getSensorReading() { return sensorReading; }
  public void setSensorReading(SensorReading sensorReading) { this.sensorReading = sensorReading; }
}