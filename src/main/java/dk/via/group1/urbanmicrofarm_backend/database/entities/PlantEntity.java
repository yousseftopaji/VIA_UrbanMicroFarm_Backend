package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "plant", schema = "urban_micro_farm_app")
public class PlantEntity {

  @Id
  private String name;

  private String description;
  private String type;
  private Instant datePlanted;
  private String status;
  private String unit;

  @Column(nullable = false)
  private Long setupId;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public Instant getDatePlanted() { return datePlanted; }
  public void setDatePlanted(Instant datePlanted) { this.datePlanted = datePlanted; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }

  public Long getSetupId() { return setupId; }
  public void setSetupId(Long setupId) { this.setupId = setupId; }
}