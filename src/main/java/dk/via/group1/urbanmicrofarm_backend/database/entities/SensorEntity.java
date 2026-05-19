package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "sensor", schema = "urban_micro_farm_app")
public class SensorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String unit;

  @Column(nullable = false)
  private String serialNumber;

  @ManyToOne
  @JoinColumn(name = "serialNumber", referencedColumnName = "serialNumber", insertable = false, updatable = false)
  private GrowingSetupEntity growingSetup;

  @Column(nullable = false)
  private String sensorTypeName;

  @ManyToOne
  @JoinColumn(name = "sensorTypeName", referencedColumnName = "name", insertable = false, updatable = false)
  private SensorTypeEntity sensorType;

  @OneToMany(mappedBy = "sensor")
  private java.util.List<SensorReadingEntity> sensorReadings;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }

  public String getSerialNumber() { return serialNumber; }
  public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

  public GrowingSetupEntity getGrowingSetup() { return growingSetup; }
  public void setGrowingSetup(GrowingSetupEntity growingSetup) { this.growingSetup = growingSetup; }

  public String getSensorTypeName() { return sensorTypeName; }
  public void setSensorTypeName(String sensorTypeName) { this.sensorTypeName = sensorTypeName; }

  public SensorTypeEntity getSensorType() { return sensorType; }
  public void setSensorType(SensorTypeEntity sensorType) { this.sensorType = sensorType; }
}