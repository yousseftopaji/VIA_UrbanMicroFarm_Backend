package dto.databaseDTOs;

public class GrowingSetup {
  private final int setupId;
  private final String serialNumber;
  private final String location;

  public GrowingSetup(int setupId, String serialNumber, String location) {
    this.setupId = setupId;
    this.serialNumber = serialNumber;
    this.location = location;
  }

  public int getSetupId() { return setupId; }
  public String getSerialNumber() { return serialNumber; }
  public String getLocation() { return location; }
}