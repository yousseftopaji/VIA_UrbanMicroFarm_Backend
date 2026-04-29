package dk.via.group1.urbanmicrofarm_backend.logic.domain;

import java.time.LocalDateTime;

public class SensorReading {
    private final Sensor sensor;
    private final double value;
    private final LocalDateTime timestamp;

    public SensorReading(Sensor sensor, double value, LocalDateTime timestamp) {
        this.sensor = sensor;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}


