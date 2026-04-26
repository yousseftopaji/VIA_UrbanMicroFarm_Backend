package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "growing_setup", schema = "urban_micro_farm_app")
public class GrowingSetup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int setupId;

  @Column(nullable = false)
  private String serialNumber;

  private String location;

  @OneToMany(mappedBy = "growingSetup", cascade = CascadeType.ALL)
  private List<Sensor> sensors;

  public GrowingSetup() {}

  public GrowingSetup(int setupId, String serialNumber, String location, List<Sensor> sensors) {
    this.setupId = setupId;
    this.serialNumber = serialNumber;
    this.location = location;
    this.sensors = sensors;
  }

  public int getSetupId() { return setupId; }
  public void setSetupId(int setupId) { this.setupId = setupId; }

  public String getSerialNumber() { return serialNumber; }
  public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public List<Sensor> getSensors() { return sensors; }
  public void setSensors(List<Sensor> sensors) { this.sensors = sensors; }
}