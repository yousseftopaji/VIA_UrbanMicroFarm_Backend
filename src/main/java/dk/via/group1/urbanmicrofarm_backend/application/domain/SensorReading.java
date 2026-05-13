package dk.via.group1.urbanmicrofarm_backend.application.domain;

import java.time.Instant;
import java.time.LocalDateTime;

public class SensorReading {
    private final Sensor sensor;
    private final double value;
    private final Instant timestamp;

    public SensorReading(Sensor sensor, double value, Instant timestamp) {
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

    public Instant getTimestamp() {
        return timestamp;
    }
}


