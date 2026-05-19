package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "prediction", schema = "urban_micro_farm_app")
public class PredictionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long predictionId;

  @Column(nullable = false)
  private Double predictedValue;

  private Instant createdAt;

  @Column(nullable = false)
  private String plantName;

    // JPA requires a no-argument constructor with at least protected visibility
    protected PredictionEntity() {
    }

    // Set createdAt automatically when the entity is first persisted
    @PrePersist
    private void onPrePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    public Long getPredictionId() { return predictionId; }
  public void setPredictionId(Long predictionId) { this.predictionId = predictionId; }

  public Double getPredictedValue() { return predictedValue; }
  public void setPredictedValue(Double predictedValue) { this.predictedValue = predictedValue; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public String getPlantName() { return plantName; }
  public void setPlantName(String plantName) { this.plantName = plantName; }
}