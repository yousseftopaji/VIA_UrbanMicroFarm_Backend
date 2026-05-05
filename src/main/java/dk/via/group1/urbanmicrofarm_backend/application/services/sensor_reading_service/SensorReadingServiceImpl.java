package dk.via.group1.urbanmicrofarm_backend.application.services.sensor_reading_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.via.group1.urbanmicrofarm_backend.database.entities.SensorReadingEntity;
import dk.via.group1.urbanmicrofarm_backend.database.repository.SensorReadingRepository;
import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.application.domain.Sensor;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorReading;
import dk.via.group1.urbanmicrofarm_backend.application.domain.SensorType;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.ActuatorCommandDto;
import dk.via.group1.urbanmicrofarm_backend.mapper.dbMapper.SensorReadingPersistenceMapper;
import dk.via.group1.urbanmicrofarm_backend.mapper.mlMapper.WaterPredictionMapper;
import dk.via.group1.urbanmicrofarm_backend.mlClient.MLPredictionClient;
import dk.via.group1.urbanmicrofarm_backend.mqtt.publisher.MqttPublisher;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorReadingServiceImpl implements SensorReadingService {

    private static final double SOIL_MOISTURE_THRESHOLD_PERCENT = 20.0; //our soil moisture threshold 20%


    private final SensorReadingRepository sensorReadingRepository;
    private final SensorReadingPersistenceMapper sensorReadingPersistenceMapper;
    private final WaterPredictionMapper waterPredictionMapper;
    private final MLPredictionClient mlPredictionClient;
    private final MqttPublisher mqttPublisher;
    private final ObjectMapper objectMapper;

    public SensorReadingServiceImpl(
            SensorReadingRepository sensorReadingRepository,
            SensorReadingPersistenceMapper sensorReadingPersistenceMapper,
            WaterPredictionMapper waterPredictionMapper,
            MLPredictionClient mlPredictionClient,
            MqttPublisher mqttPublisher,
            ObjectMapper objectMapper) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorReadingPersistenceMapper = sensorReadingPersistenceMapper;
        this.waterPredictionMapper = waterPredictionMapper;
        this.mlPredictionClient = mlPredictionClient;
        this.mqttPublisher = mqttPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void processReadings(TelemetryData telemetryData) {
        validate(telemetryData); // we check the data first

        LocalDateTime timestamp = LocalDateTime.now(); // we make timestamp of time now

        // we get the values from telemetryData and we convert it
        double temperature = telemetryData.temperature() / 10.0;
        double humidity = telemetryData.humidity() / 10.0;
        int light = telemetryData.light();
        int soilMoistureRaw = telemetryData.soilMoisture();

        // we will make our list of sensor readings
        List<SensorReading> readings = createSensorReadings(
                telemetryData,
                timestamp,
                temperature,
                humidity,
                light,
                soilMoistureRaw
        );

        // we map our list of readings to db entities
        List<SensorReadingEntity> entities = readings.stream()
                .map(reading -> sensorReadingPersistenceMapper.toEntity(telemetryData.setupId(), reading))
                .toList();

        sensorReadingRepository.saveAll(entities); // we save the readings to db

        double soilMoisturePercent = convertSoilMoistureToPercent(soilMoistureRaw); // we convert soil moisture to percantage

        // if we the reading is bellow threshold than call for predictinng
        if (soilMoisturePercent < SOIL_MOISTURE_THRESHOLD_PERCENT) {
            requestPredictionAndWater(telemetryData, temperature, humidity, light, soilMoistureRaw);
        }

    }


    // helping method to create the list of sensor readings
    private List<SensorReading> createSensorReadings(TelemetryData telemetryData, LocalDateTime timestamp,
                                                     double temperature, double humidity, int light, int soilMoistureRaw) {

        List<SensorReading> readings = new ArrayList<>(); // we create empty list of sensor readings

        // we create entity of Sensor
        Sensor temperatureSensor = new Sensor(telemetryData.sensorId(), SensorType.TEMPERATURE, "C");
        Sensor humiditySensor = new Sensor(telemetryData.sensorId(), SensorType.HUMIDITY, "%");
        Sensor lightSensor = new Sensor(telemetryData.sensorId(), SensorType.LIGHT, "ADC");
        Sensor soilMoistureSensor = new Sensor(telemetryData.sensorId(), SensorType.SOIL_MOISTURE, "ADC");

        // and we create and add readings to the list
        readings.add(new SensorReading(temperatureSensor, temperature, timestamp));
        readings.add(new SensorReading(humiditySensor, humidity, timestamp));
        readings.add(new SensorReading(lightSensor, light, timestamp));
        readings.add(new SensorReading(soilMoistureSensor, soilMoistureRaw, timestamp));

        return readings;
    }

    // method for creating the "actuator command"
    private void requestPredictionAndWater(
            TelemetryData telemetryData,
            double temperature,
            double humidity,
            int light,
            int soilMoistureRaw) {

        WaterPredictionRequestDto request = waterPredictionMapper.toRequestDto(
                temperature,
                humidity,
                light,
                soilMoistureRaw
        );

        WaterPredictionResponseDto response = mlPredictionClient.predictWater(request);

        publishWaterCommand(
                telemetryData.setupId(),
                response.wateringAmount()
        );
    }

    //method to send the actuator command to the mqtt publisher
    private void publishWaterCommand(int setupId, int amountMl) {
        String topic = "farm/" + setupId + "/cmd";

        ActuatorCommandDto command = new ActuatorCommandDto(
                "water_pump",
                amountMl
        );

        try {
            String payload = objectMapper.writeValueAsString(command);
            mqttPublisher.publish(topic, payload);
        } catch (MqttException e) {
            throw new IllegalStateException("Could not publish actuator command", e);
        }
    }


    //helping method the convert soil moisture to percentages
    private double convertSoilMoistureToPercent(int rawSoilMoisture) {
        return (rawSoilMoisture / 1023.0) * 100.0;
    }

    private void validate(TelemetryData telemetryData) {
        if (telemetryData.setupId() <= 0) {
            throw new IllegalArgumentException("Invalid setup id");
        }

        if (telemetryData.sensorId() == null || telemetryData.sensorId() <= 0) {
            throw new IllegalArgumentException("Invalid sensor id");
        }
    }
}