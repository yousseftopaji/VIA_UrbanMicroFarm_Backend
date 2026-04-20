package logic.domain;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

public class SensorReading {
    private final SensorType sensorType;
    private final double value;
    private final LocalDateTime timestamp;

    public SensorReading(SensorType sensorType, double value, LocalDateTime timestamp) {
        this.sensorType = sensorType;
        this.value = value;
        this.timestamp = timestamp;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public double getValue() {
        return value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

