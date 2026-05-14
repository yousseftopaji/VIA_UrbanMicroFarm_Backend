package dk.via.group1.urbanmicrofarm_backend.application.domain;

import java.util.List;

public class GrowingSetup {
    private final int setupId;
    private final String serialNumber;
    private final String location;
    private String status;
    private final List<Sensor> sensors;

    public GrowingSetup(int setupId, String serialNumber, String location, List<Sensor> sensors) {
        this.setupId = setupId;
        this.serialNumber = serialNumber;
        this.location = location;
        status = "";
        this.sensors = sensors;
    }

    public int getSetupId() {
        return setupId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {    return status;
  }

    public void setStatus(String status)
    {
     this.status = status;
    }

    public List<Sensor> getSensors() {
        return sensors;

    }
}