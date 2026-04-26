package dto.databaseDTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prediction {
  private final int id;
  private final double predictedValue;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final LocalDateTime createdAt;
  private final int plantId;

  public Prediction(int id, double predictedValue, LocalDate startDate, LocalDate endDate, LocalDateTime createdAt, int plantId) {
    this.id = id;
    this.predictedValue = predictedValue;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createdAt = createdAt;
    this.plantId = plantId;
  }

  public int getId() { return id; }
  public double getPredictedValue() { return predictedValue; }
  public LocalDate getStartDate() { return startDate; }
  public LocalDate getEndDate() { return endDate; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public int getPlantId() { return plantId; }
}