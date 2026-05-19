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
  private String serialNumber;

  @ManyToOne
  @JoinColumn(name = "serialNumber", referencedColumnName = "serialNumber", insertable = false, updatable = false)
  private GrowingSetupEntity growingSetup;

  @Column(nullable = false)
  private String sensorTypeName;

  public Long getSensorId() { return sensorId; }
  public void setSensorId(Long sensorId) { this.sensorId = sensorId; }

  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }

  public String getSerialNumber() { return serialNumber; }
  public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

  public GrowingSetupEntity getGrowingSetup() { return growingSetup; }
  public void setGrowingSetup(GrowingSetupEntity growingSetup) { this.growingSetup = growingSetup; }

  public String getSensorTypeName() { return sensorTypeName; }
  public void setSensorTypeName(String sensorTypeName) { this.sensorTypeName = sensorTypeName; }
}