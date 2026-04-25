package dk.via.group1.urbanmicrofarm_backend.aplication.controller;

import dk.via.group1.urbanmicrofarm_backend.aplication.mapper.SensorReadingApiMapper;
import dk.via.group1.urbanmicrofarm_backend.aplication.services.sensor_reading_service.SensorReadingQueryService;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingHistoryResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingLatestResponseDto;
import dk.via.group1.urbanmicrofarm_backend.aplication.mapper.SensorReadingPersistenceMapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensor-readings")
public class SensorReadingController {

    private final SensorReadingQueryService sensorReadingQueryService;
    private final SensorReadingApiMapper sensorReadingApiMapper;

    public SensorReadingController(
            SensorReadingQueryService sensorReadingQueryService,
            SensorReadingApiMapper sensorReadingApiMapper) {
        this.sensorReadingQueryService = sensorReadingQueryService;
        this.sensorReadingApiMapper = sensorReadingApiMapper;
    }

    @GetMapping("/setups/{setupId}/sensors/{sensorType}/latest")
    public SensorReadingLatestResponseDto getLatestReading(
            @PathVariable Integer setupId,
            @PathVariable String sensorType) {

        return sensorReadingQueryService.getLatestReading(setupId, sensorType)
                .map(sensorReadingApiMapper::toLatestResponseDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sensor reading not found for setupId: " + setupId + ", sensorType: " + sensorType
                ));
    }

    @GetMapping("/setups/{setupId}/sensors/{sensorType}/history")
    public SensorReadingHistoryResponseDto getHistoricalReadings(
            @PathVariable Integer setupId,
            @PathVariable String sensorType) {

        return sensorReadingApiMapper.toHistoryResponseDto(
                setupId,
                sensorType,
                sensorReadingQueryService.getHistoricalReadings(setupId, sensorType)
        );
    }
}