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

  private Instant startDate;
  private Instant endDate;
  private Instant createdAt;

  @Column(nullable = false)
  private String plantName;

  public Long getPredictionId() { return predictionId; }
  public void setPredictionId(Long predictionId) { this.predictionId = predictionId; }

  public Double getPredictedValue() { return predictedValue; }
  public void setPredictedValue(Double predictedValue) { this.predictedValue = predictedValue; }

  public Instant getStartDate() { return startDate; }
  public void setStartDate(Instant startDate) { this.startDate = startDate; }

  public Instant getEndDate() { return endDate; }
  public void setEndDate(Instant endDate) { this.endDate = endDate; }

  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

  public String getPlantName() { return plantName; }
  public void setPlantName(String plantName) { this.plantName = plantName; }
}