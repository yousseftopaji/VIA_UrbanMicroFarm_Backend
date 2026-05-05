package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "sensor", schema = "urban_micro_farm_app")
public class SensorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long sensorId;

  @Column(nullable = false)
  private String unit;

  @Column(nullable = false)
  private Long setupId;

  @Column(nullable = false)
  private String sensorTypeName;

  public Long getSensorId() { return sensorId; }
  public void setSensorId(Long sensorId) { this.sensorId = sensorId; }

  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }

  public Long getSetupId() { return setupId; }
  public void setSetupId(Long setupId) { this.setupId = setupId; }

  public String getSensorTypeName() { return sensorTypeName; }
  public void setSensorTypeName(String sensorTypeName) { this.sensorTypeName = sensorTypeName; }
}