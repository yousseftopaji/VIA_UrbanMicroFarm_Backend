package dk.via.group1.urbanmicrofarm_backend.sensorreading.controller;

import dk.via.group1.urbanmicrofarm_backend.sensorreading.dto.SensorReadingHistoryResponseDto;
import dk.via.group1.urbanmicrofarm_backend.sensorreading.dto.SensorReadingLatestResponseDto;
import dk.via.group1.urbanmicrofarm_backend.sensorreading.mapper.SensorReadingApiMapper;
import dk.via.group1.urbanmicrofarm_backend.sensorreading.service.SensorReadingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensor-readings")
public class SensorReadingController
{
  private final SensorReadingService sensorReadingService;
  private final SensorReadingApiMapper sensorReadingApiMapper;

  public SensorReadingController(SensorReadingService sensorReadingService,
      SensorReadingApiMapper sensorReadingApiMapper)
  {
    this.sensorReadingService = sensorReadingService;
    this.sensorReadingApiMapper = sensorReadingApiMapper;
  }

  @GetMapping("/sensors/{sensorId}/latest")
  public SensorReadingLatestResponseDto getLatestReading(
      @PathVariable String sensorId)
  {
    return sensorReadingService.getLatestReading(sensorId)
        .map(sensorReadingApiMapper::toLatestResponseDto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Sensor reading not found for sensorId: " + sensorId));
  }

  @GetMapping("/sensors/{sensorId}/history")
  public SensorReadingHistoryResponseDto getHistoricalReadings(
      @PathVariable String sensorId)
  {
    return sensorReadingApiMapper.toHistoryResponseDto(sensorId,
        sensorReadingService.getHistoricalReadings(sensorId));
  }
}