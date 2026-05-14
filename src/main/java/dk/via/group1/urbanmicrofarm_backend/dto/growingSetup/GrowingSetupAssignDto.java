package dk.via.group1.urbanmicrofarm_backend.dto.growingSetup;

public class GrowingSetupAssignDto
{
  private int userId;
  private int setupId;

  public int getUserId() { return userId; }
  public void setUserId(int userId) { this.userId = userId; }

  public int getSetupId() { return setupId; }
  public void setSetupId(int setupId) { this.setupId = setupId; }
}