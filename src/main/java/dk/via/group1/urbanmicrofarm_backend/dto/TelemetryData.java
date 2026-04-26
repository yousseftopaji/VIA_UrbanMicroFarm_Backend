package dk.via.group1.urbanmicrofarm_backend.dto;

public record TelemetryData(int setupId, Integer sensorId, int temperature, int humidity,
                            int light, int soilMoisture)
{
}
