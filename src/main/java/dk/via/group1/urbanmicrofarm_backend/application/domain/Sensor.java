package dk.via.group1.urbanmicrofarm_backend.application.domain;

public class Sensor {
    private final int sensorId;
    private final SensorType type;
    private final String unit;

    public Sensor(int sensorId, SensorType type, String unit) {
        this.sensorId = sensorId;
        this.type = type;
        this.unit = unit;
    }

    public int getSensorId() {
        return sensorId;
    }

    public SensorType getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }
}
