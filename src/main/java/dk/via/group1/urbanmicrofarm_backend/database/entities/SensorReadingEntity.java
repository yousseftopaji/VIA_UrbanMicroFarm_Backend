package dk.via.group1.urbanmicrofarm_backend.database.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "sensor_readings")
public class SensorReadingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long readingId;

    @Column(nullable = false)
    private Integer setupId;

    @Column(nullable = false)
    private Integer sensorId;

    @Column(nullable = false)
    private String sensorType;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private Instant timestamp;

    public Long getReadingId() {
        return readingId;
    }

    public void setReadingId(Long readingId) {
        this.readingId = readingId;
    }

    public Integer getSetupId() {
        return setupId;
    }

    public void setSetupId(Integer setupId) {
        this.setupId = setupId;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
