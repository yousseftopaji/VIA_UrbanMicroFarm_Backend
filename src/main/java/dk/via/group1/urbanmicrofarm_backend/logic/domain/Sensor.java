package dk.via.group1.urbanmicrofarm_backend.logic.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "sensor", schema = "urban_micro_farm_app")
public class Sensor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int sensorId;

  @Enumerated(EnumType.STRING)
  private SensorType type;

  private String unit;

  @ManyToOne
  @JoinColumn(name = "setup_id")
  private GrowingSetup growingSetup;

  @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
  private List<SensorReading> sensorReadings;

  public Sensor() {}

  public Sensor(int sensorId, SensorType type, String unit) {
    this.sensorId = sensorId;
    this.type = type;
    this.unit = unit;
  }

  public int getSensorId() { return sensorId; }
  public void setSensorId(int sensorId) { this.sensorId = sensorId; }

  public SensorType getType() { return type; }
  public void setType(SensorType type) { this.type = type; }

  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }

  public GrowingSetup getGrowingSetup() { return growingSetup; }
  public void setGrowingSetup(GrowingSetup growingSetup) { this.growingSetup = growingSetup; }

  public List<SensorReading> getSensorReadings() { return sensorReadings; }
  public void setSensorReadings(List<SensorReading> readings) { this.sensorReadings = readings; }
}