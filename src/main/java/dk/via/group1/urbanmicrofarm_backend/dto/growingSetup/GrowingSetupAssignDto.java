package dk.via.group1.urbanmicrofarm_backend.dto.growingSetup;

public class GrowingSetupAssignDto
{
  private Long userId;
  private String serialNumber;

  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }

  public String getSerialNumber() { return serialNumber; }
  public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
}