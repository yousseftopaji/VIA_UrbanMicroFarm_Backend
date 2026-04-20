package dk.via.group1.urbanmicrofarm_backend.sensorreading.service;

import dk.via.group1.urbanmicrofarm_backend.sensorreading.model.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.sensorreading.repository.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingService
{
  private final SensorReadingRepository sensorReadingRepository;

  public SensorReadingService(SensorReadingRepository sensorReadingRepository)
  {
    this.sensorReadingRepository = sensorReadingRepository;
  }

  public Optional<SensorReading> getLatestReading(String sensorId)
  {
    return sensorReadingRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId);
  }

  public List<SensorReading> getHistoricalReadings(String sensorId)
  {
    return sensorReadingRepository.findBySensorIdOrderByTimestampDesc(sensorId);
  }
}
