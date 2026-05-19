package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "watering_event", schema = "urban_micro_farm_app")
public class WateringEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double waterUsed_mL;
  private String mode;

  @Column(nullable = false)
  private Long actuatorId;

  @ManyToOne
  @JoinColumn(name = "actuatorId", referencedColumnName = "id", insertable = false, updatable = false)
  private ActuatorEntity actuator;

  @OneToOne(mappedBy = "wateringEvent")
  private AlertEntity alert;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Double getWaterUsed_mL() { return waterUsed_mL; }
  public void setWaterUsed_mL(Double waterUsed_mL) { this.waterUsed_mL = waterUsed_mL; }

  public String getMode() { return mode; }
  public void setMode(String mode) { this.mode = mode; }

  public Long getActuatorId() { return actuatorId; }
  public void setActuatorId(Long actuatorId) { this.actuatorId = actuatorId; }

  public ActuatorEntity getActuator() { return actuator; }
  public void setActuator(ActuatorEntity actuator) { this.actuator = actuator; }

  public AlertEntity getAlert() { return alert; }
  public void setAlert(AlertEntity alert) { this.alert = alert; }
}