package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "plant", schema = "urban_micro_farm_app")
public class PlantEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;
  private String type;
  private Instant datePlanted;
  private String status;
  private Long sensorId;

  @OneToOne
  @JoinColumn(name = "sensorId", referencedColumnName = "id", insertable = false, updatable = false)
  private SensorEntity sensor;

  @OneToMany(mappedBy = "plant")
  private List<PredictionEntity> predictions;

  @OneToOne(mappedBy = "plant")
  private ThresholdEntity threshold;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

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

   public Long getSensorId() { return sensorId; }
  public void setSensorId(Long sensorId) { this.sensorId = sensorId; }

  public SensorEntity getSensor() { return sensor; }
  public void setSensor(SensorEntity sensor) { this.sensor = sensor; }

  public List<PredictionEntity> getPredictions() { return predictions; }
  public void setPredictions(List<PredictionEntity> predictions) { this.predictions = predictions; }

  public ThresholdEntity getThreshold() { return threshold; }
  public void setThreshold(ThresholdEntity threshold) { this.threshold = threshold; }
}