package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "sensor_type", schema = "urban_micro_farm_app")
public class SensorTypeEntity {

  @Id
  private String name;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
}