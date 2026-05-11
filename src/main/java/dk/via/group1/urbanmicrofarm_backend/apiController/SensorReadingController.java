package dk.via.group1.urbanmicrofarm_backend.apiController;

import dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service.SensorReadingQueryService;
import dk.via.group1.urbanmicrofarm_backend.mapper.apiMapper.SensorReadingApiMapper;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingHistoryResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.SensorReadingLatestResponseDto;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestController
@RequestMapping("/api/sensors")
public class SensorReadingController {

    private final SensorReadingQueryService sensorReadingQueryService;
    private final SensorReadingApiMapper sensorReadingApiMapper;

    public SensorReadingController(
            SensorReadingQueryService sensorReadingQueryService,
            SensorReadingApiMapper sensorReadingApiMapper) {
        this.sensorReadingQueryService = sensorReadingQueryService;
        this.sensorReadingApiMapper = sensorReadingApiMapper;
    }

    @GetMapping("/{sensorId}/readings/latest")
    public SensorReadingLatestResponseDto getLatestReading(
            @PathVariable Integer sensorId) {

        return sensorReadingQueryService.getLatestReading(sensorId)
                .map(sensorReadingApiMapper::toLatestResponseDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sensor reading not found for sensorId: " + sensorId
                ));
    }

    /**
     * @param sensorId - Required. Indicates sensor id of which readings are looked for.
     * @param from - Optional parameter. Indicates the beginning of time range of sensor readings.
     * @param to - Optional parameter. Indicates the end of time range of sensor readings.
     *
     * @return Returns a list of sensor readings of sensor with @sensorId, and, if provided, a time range within @from and @to
     */
    @GetMapping("/{sensorId}/readings")
    public SensorReadingHistoryResponseDto getHistoricalReadings(
            @PathVariable Integer sensorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {

        System.out.println(sensorId);

        return sensorReadingApiMapper.toHistoryResponseDto(
                sensorId,
                sensorReadingQueryService.getHistoricalReadings(sensorId, from, to)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException exception) {
        return exception.getMessage();
    }
}
