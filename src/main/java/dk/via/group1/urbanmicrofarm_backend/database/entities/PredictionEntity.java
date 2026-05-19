package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "prediction", schema = "urban_micro_farm_app")
public class PredictionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Double predictedValue;

  @Column(nullable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Long plantId;

  @ManyToOne
  @JoinColumn(name = "plantId", referencedColumnName = "id", insertable = false, updatable = false)
  private PlantEntity plant;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Double getPredictedValue() { return predictedValue; }
  public void setPredictedValue(Double predictedValue) { this.predictedValue = predictedValue; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public Long getPlantId() { return plantId; }
  public void setPlantId(Long plantId) { this.plantId = plantId; }

  public PlantEntity getPlant() { return plant; }
  public void setPlant(PlantEntity plant) { this.plant = plant; }
}