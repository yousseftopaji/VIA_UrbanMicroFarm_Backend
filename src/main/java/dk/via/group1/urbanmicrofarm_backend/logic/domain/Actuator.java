package dk.via.group1.urbanmicrofarm_backend.logic.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "actuator", schema = "urban_micro_farm_app")
public class Actuator {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "actuator_type", nullable = false)
  private String type;

  private String status;

  @OneToOne
  @JoinColumn(name = "setup_id", nullable = false)
  private GrowingSetup growingSetup;

  @OneToMany(mappedBy = "actuator", cascade = CascadeType.ALL)
  private List<WateringEvent> wateringEvents;

  public Actuator() {}

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public GrowingSetup getGrowingSetup() { return growingSetup; }
  public void setGrowingSetup(GrowingSetup growingSetup) { this.growingSetup = growingSetup; }

  public List<WateringEvent> getWateringEvents() { return wateringEvents; }
  public void setWateringEvents(List<WateringEvent> wateringEvents) { this.wateringEvents = wateringEvents; }
}