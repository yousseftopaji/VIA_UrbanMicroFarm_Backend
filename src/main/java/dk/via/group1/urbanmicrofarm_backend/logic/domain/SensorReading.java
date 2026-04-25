package dk.via.group1.urbanmicrofarm_backend.logic.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sensor_reading", schema = "urban_micro_farm_app")
public class SensorReading {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "reading_value", nullable = false)
  private double value;

  @Column(name = "reading_timestamp", nullable = false)
  private LocalDateTime timestamp;

  @ManyToOne
  @JoinColumn(name = "sensor_id", nullable = false)
  private Sensor sensor;

  @OneToMany(mappedBy = "sensorReading", cascade = CascadeType.ALL)
  private List<Alert> alerts;

  @OneToOne(mappedBy = "sensorReading", cascade = CascadeType.ALL)
  private Threshold threshold;

  public SensorReading() {}

  public SensorReading(Sensor sensor, double value, LocalDateTime timestamp) {
    this.sensor = sensor;
    this.value = value;
    this.timestamp = timestamp;
  }

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public double getValue() { return value; }
  public void setValue(double value) { this.value = value; }

  public LocalDateTime getTimestamp() { return timestamp; }
  public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

  public Sensor getSensor() { return sensor; }
  public void setSensor(Sensor sensor) { this.sensor = sensor; }

  public List<Alert> getAlerts() { return alerts; }
  public void setAlerts(List<Alert> alerts) { this.alerts = alerts; }

  public Threshold getThreshold() { return threshold; }
  public void setThreshold(Threshold threshold) { this.threshold = threshold; }
}


