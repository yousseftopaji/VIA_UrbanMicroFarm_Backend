package dk.via.group1.urbanmicrofarm_backend.apiController;

import dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service.SensorReadingQueryService;
import dk.via.group1.urbanmicrofarm_backend.mapper.apiMapper.SensorReadingApiMapper;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingHistoryResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingLatestResponseDto;


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

    @GetMapping("/setups/{serialNumber}/sensors/{sensorType}/latest")
    public SensorReadingLatestResponseDto getLatestReading(
            @PathVariable String serialNumber,
            @PathVariable String sensorType) {

        return sensorReadingQueryService.getLatestReading(serialNumber, sensorType)
                .map(sensorReading -> sensorReadingApiMapper.toLatestResponseDto(serialNumber, sensorReading))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sensor reading not found for serialNumber: " + serialNumber + ", sensorType: " + sensorType
                ));
    }

    @GetMapping("/setups/{serialNumber}/sensors/{sensorType}/history")
    public SensorReadingHistoryResponseDto getHistoricalReadings(
            @PathVariable String serialNumber,
            @PathVariable String sensorType) {

        return sensorReadingApiMapper.toHistoryResponseDto(
                serialNumber,
                sensorType,
                sensorReadingQueryService.getHistoricalReadings(serialNumber, sensorType)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException exception) {
        return exception.getMessage();
    }
}
