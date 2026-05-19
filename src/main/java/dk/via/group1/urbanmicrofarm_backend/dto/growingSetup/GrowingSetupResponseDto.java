package dk.via.group1.urbanmicrofarm_backend.dto.growingSetup;

public class GrowingSetupResponseDto {
  private GrowingSetupDetails growingSetup;

  public GrowingSetupResponseDto(GrowingSetupDetails growingSetup) {
    this.growingSetup = growingSetup;
  }

  public GrowingSetupDetails getGrowingSetup() { return growingSetup; }
  public void setGrowingSetup(GrowingSetupDetails growingSetup) { this.growingSetup = growingSetup; }

  public static class GrowingSetupDetails {
    private String id;
    private String location;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
  }
}