package dk.via.group1.urbanmicrofarm_backend.application.test;

import dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service.SensorReadingService;
import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test-runner")
@Component
public class SensorReadingTestRunner implements CommandLineRunner {

    private final SensorReadingService sensorReadingService;

    public SensorReadingTestRunner(SensorReadingService sensorReadingService) {
        this.sensorReadingService = sensorReadingService;
    }

    @Override
    public void run(String... args) {
        TelemetryData telemetryData = new TelemetryData(
                1,      // setupId
                1,   // sensorId
                225,    // temperature: 22.5°C
                650,    // humidity: 65.0%
                800,    // light ADC
                100     // soil moisture ADC, around 9.8%, should trigger watering
        );

        sensorReadingService.processReadings(telemetryData);

        System.out.println("Test telemetry processed successfully.");
    }
}