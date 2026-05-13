package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "water_amount_prediction",
        schema = "urban_micro_farm_app"
)
public class WaterAmountPredictionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer wateringAmount;

    private Instant createdAt;

    // added a comment 
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Integer getWateringAmount() {
        return wateringAmount;
    }

    public void setWateringAmount(Integer wateringAmount) {
        this.wateringAmount = wateringAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}