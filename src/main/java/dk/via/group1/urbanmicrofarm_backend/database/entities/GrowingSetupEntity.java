package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "growing_setup", schema = "urban_micro_farm_app")
public class GrowingSetupEntity {

  @Id
  @Column(nullable = false, unique = true)
  private String serialNumber;

  private String location;

  @Column(nullable = false)
  private Long userId;

  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
  private UserEntity user;

  public String getSerialNumber() { return serialNumber; }
  public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }

  public UserEntity getUser() { return user; }
  public void setUser(UserEntity user) { this.user = user; }
}
