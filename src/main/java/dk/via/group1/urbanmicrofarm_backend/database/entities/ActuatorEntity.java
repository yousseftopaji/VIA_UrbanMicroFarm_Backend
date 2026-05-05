package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "actuator", schema = "urban_micro_farm_app")
public class ActuatorEntity {

  @Id
  private String type;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false)
  private Long setupId;

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public Long getSetupId() { return setupId; }
  public void setSetupId(Long setupId) { this.setupId = setupId; }
}
