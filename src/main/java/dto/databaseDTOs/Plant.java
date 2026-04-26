package dto.databaseDTOs;

import java.time.LocalDate;

public class Plant {
  private final int id;
  private final String name;
  private final String description;
  private final String type;
  private final LocalDate datePlanted;
  private final String status;
  private final String unit;
  private final int setupId;

  public Plant(int id, String name, String description, String type, LocalDate datePlanted, String status, String unit, int setupId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
    this.datePlanted = datePlanted;
    this.status = status;
    this.unit = unit;
    this.setupId = setupId;
  }

  public int getId() { return id; }
  public String getName() { return name; }
  public String getDescription() { return description; }
  public String getType() { return type; }
  public LocalDate getDatePlanted() { return datePlanted; }
  public String getStatus() { return status; }
  public String getUnit() { return unit; }
  public int getSetupId() { return setupId; }
}