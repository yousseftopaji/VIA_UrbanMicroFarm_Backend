package dto.databaseDTOs;

public class Actuator {
  private final int id;
  private final String type;
  private final String status;
  private final int setupId;

  public Actuator(int id, String type, String status, int setupId) {
    this.id = id;
    this.type = type;
    this.status = status;
    this.setupId = setupId;
  }

  public int getId() { return id; }
  public String getType() { return type; }
  public String getStatus() { return status; }
  public int getSetupId() { return setupId; }
}