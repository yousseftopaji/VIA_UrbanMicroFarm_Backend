package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "growing_setup", schema = "urban_micro_farm_app")
public class GrowingSetupEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int setupId;

  @Column(nullable = false)
  private String serialNumber;

  private String location;

  @Column
  private String email;

  public int getSetupId() { return setupId; }
  public void setSetupId(int setupId) { this.setupId = setupId; }

  public String getSerialNumber() { return serialNumber; }
  public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
}
