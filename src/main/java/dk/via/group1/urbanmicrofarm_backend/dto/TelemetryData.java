package dk.via.group1.urbanmicrofarm_backend.dto;

public class TelemetryData {
    private final int setupId;
    private final Integer sensorId;
    private final int temperature;
    private final int humidity;
    private final int light;
    private final int soilMoisture;

    public TelemetryData(int setupId, Integer sensorId, int temperature, int humidity, int light, int soilMoisture) {
        this.setupId = setupId;
        this.sensorId = sensorId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.soilMoisture = soilMoisture;
    }

    public int getSetupId() { return setupId; }
    public Integer getSensorId() { return sensorId; }
    public int getTemperature() { return temperature; }
    public int getHumidity() { return humidity; }
    public int getLight() { return light; }
    public int getSoilMoisture() { return soilMoisture; }
}
