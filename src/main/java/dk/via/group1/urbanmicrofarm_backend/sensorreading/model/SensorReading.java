package dk.via.group1.urbanmicrofarm_backend.sensorreading.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "sensor_readings")
public class SensorReading
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long readingId;

  @Column(nullable = false)
  private String sensorId;

  @Column(nullable = false)
  private Double value;

  // Instant matches the API's UTC `...Z` timestamp format and avoids timezone misunderstandings.
  @Column(nullable = false)
  private Instant timestamp;

  public Long getReadingId()
  {
    return readingId;
  }

  public void setReadingId(Long readingId)
  {
    this.readingId = readingId;
  }

  public String getSensorId()
  {
    return sensorId;
  }

  public void setSensorId(String sensorId)
  {
    this.sensorId = sensorId;
  }

  public Double getValue()
  {
    return value;
  }

  public void setValue(Double value)
  {
    this.value = value;
  }

  public Instant getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp)
  {
    this.timestamp = timestamp;
  }
}
