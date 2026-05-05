package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "threshold", schema = "urban_micro_farm_app")
public class ThresholdEntity {

  @Id
  private String type;

  @Column(nullable = false)
  private Double value;

  private Instant createdAt;

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public Double getValue() { return value; }
  public void setValue(Double value) { this.value = value; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
