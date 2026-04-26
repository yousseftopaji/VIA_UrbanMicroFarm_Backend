package dk.via.group1.urbanmicrofarm_backend.database.entities;

import dk.via.group1.urbanmicrofarm_backend.logic.domain.GrowingSetup;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users", schema = "urban_micro_farm_app")
public class User {

  @Id
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<GrowingSetup> growingSetups;

  public User() {}

  public User(String email, String name, String password) {
    this.email = email;
    this.name = name;
    this.password = password;
  }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public List<GrowingSetup> getGrowingSetups() { return growingSetups; }
  public void setGrowingSetups(List<GrowingSetup> growingSetups) { this.growingSetups = growingSetups; }
}