package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "actuator", schema = "urban_micro_farm_app")
public class ActuatorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false)
  private String serialNumber;

  @ManyToOne
  @JoinColumn(name = "serialNumber", referencedColumnName = "serialNumber", insertable = false, updatable = false)
  private GrowingSetupEntity growingSetup;

  @OneToMany(mappedBy = "actuator")
  private List<WateringEventEntity> wateringEvents;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getSerialNumber() { return serialNumber; }
  public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

  public GrowingSetupEntity getGrowingSetup() { return growingSetup; }
  public void setGrowingSetup(GrowingSetupEntity growingSetup) { this.growingSetup = growingSetup; }

  public List<WateringEventEntity> getWateringEvents() { return wateringEvents; }
  public void setWateringEvents(List<WateringEventEntity> wateringEvents) { this.wateringEvents = wateringEvents; }
}
