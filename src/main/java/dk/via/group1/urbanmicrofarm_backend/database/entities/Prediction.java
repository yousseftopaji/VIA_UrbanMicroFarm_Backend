package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction", schema = "urban_micro_farm_app")
public class Prediction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "predicted_value")
  private Double predictedValue;

  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "plant_id", nullable = false)
  private Plant plant;

  public Prediction() {}

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public Double getPredictedValue() { return predictedValue; }
  public void setPredictedValue(Double predictedValue) { this.predictedValue = predictedValue; }

  public LocalDate getStartDate() { return startDate; }
  public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

  public LocalDate getEndDate() { return endDate; }
  public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

  public Plant getPlant() { return plant; }
  public void setPlant(Plant plant) { this.plant = plant; }
}