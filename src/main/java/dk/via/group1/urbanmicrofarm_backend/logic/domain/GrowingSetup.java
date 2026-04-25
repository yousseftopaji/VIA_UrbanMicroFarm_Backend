package dk.via.group1.urbanmicrofarm_backend.logic.domain;

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

  @ManyToOne
  @JoinColumn(name = "user_email")
  private User user;

  @OneToMany(mappedBy = "growingSetup", cascade = CascadeType.ALL)
  private List<Sensor> sensors;

  @OneToOne(mappedBy = "growingSetup", cascade = CascadeType.ALL)
  private Actuator actuator;

  @OneToMany(mappedBy = "growingSetup", cascade = CascadeType.ALL)
  private List<Plant> plants;

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

  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }

  public List<Sensor> getSensors() { return sensors; }
  public void setSensors(List<Sensor> sensors) { this.sensors = sensors; }

  public Actuator getActuator() { return actuator; }
  public void setActuator(Actuator actuator) { this.actuator = actuator; }

  public List<Plant> getPlants() { return plants; }
  public void setPlants(List<Plant> plants) { this.plants = plants; }
}