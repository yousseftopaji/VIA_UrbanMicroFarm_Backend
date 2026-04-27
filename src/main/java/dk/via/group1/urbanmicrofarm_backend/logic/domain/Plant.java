package dk.via.group1.urbanmicrofarm_backend.logic.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "plant", schema = "urban_micro_farm_app")
public class Plant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false)
  private String name;

  private String description;
  private String type;
  private LocalDate datePlanted;
  private String status;
  private String unit;

  @ManyToOne
  @JoinColumn(name = "setup_id", nullable = false)
  private GrowingSetup growingSetup;

  @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
  private List<Prediction> predictions;

  public Plant() {}

  public int getId() { return id; }
  public void setId(int id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public LocalDate getDatePlanted() { return datePlanted; }
  public void setDatePlanted(LocalDate datePlanted) { this.datePlanted = datePlanted; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public String getUnit() { return unit; }
  public void setUnit(String unit) { this.unit = unit; }

  public GrowingSetup getGrowingSetup() { return growingSetup; }
  public void setGrowingSetup(GrowingSetup growingSetup) { this.growingSetup = growingSetup; }

  public List<Prediction> getPredictions() { return predictions; }
  public void setPredictions(List<Prediction> predictions) { this.predictions = predictions; }
}