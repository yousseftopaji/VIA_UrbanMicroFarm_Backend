package logic.domain;

import java.util.List;

public class GrowingSetup {
    private final int setupId;
    private final String serialNumber;
    private final String location;
    private final List<Sensor> sensors;

    public GrowingSetup(int setupId, String serialNumber, String location, List<Sensor> sensors) {
        this.setupId = setupId;
        this.serialNumber = serialNumber;
        this.location = location;
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

    public List<Sensor> getSensors() {
        return sensors;

    }
}