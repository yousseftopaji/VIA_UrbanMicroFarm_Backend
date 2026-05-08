package dk.via.group1.urbanmicrofarm_backend.application.services.watering;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionRequestDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mlDto.WaterPredictionResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.ActuatorCommandDto;
import dk.via.group1.urbanmicrofarm_backend.mapper.mlMapper.WaterPredictionMapper;
import dk.via.group1.urbanmicrofarm_backend.mlClient.MLPredictionClient;
import dk.via.group1.urbanmicrofarm_backend.mqtt.publisher.MqttPublisher;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class WateringAutomationServiceImpl implements WateringAutomationService {

    private static final double SOIL_MOISTURE_THRESHOLD_PERCENT = 20.0; // our soil moisture threshold 20%

    private final WaterPredictionMapper waterPredictionMapper;
    private final MLPredictionClient mlPredictionClient;
    private final MqttPublisher mqttPublisher;
    private final ObjectMapper objectMapper;

    public WateringAutomationServiceImpl(
            WaterPredictionMapper waterPredictionMapper,
            MLPredictionClient mlPredictionClient,
            MqttPublisher mqttPublisher,
            ObjectMapper objectMapper) {
        this.waterPredictionMapper = waterPredictionMapper;
        this.mlPredictionClient = mlPredictionClient;
        this.mqttPublisher = mqttPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleWateringIfNeeded(TelemetryData telemetryData) {
        // we convert raw soil moisture from ADC value to percentage
        double soilMoisturePercent = convertSoilMoistureToPercent(telemetryData.soilMoisture());

        // if the soil moisture is not below threshold, we do not need to water
        if (soilMoisturePercent >= SOIL_MOISTURE_THRESHOLD_PERCENT) {
            return;
        }

        // we get the values from telemetryData and convert temperature and humidity
        double temperature = telemetryData.temperature() / 10.0;
        double humidity = telemetryData.humidity() / 10.0;

        // we map telemetry values to ML request DTO
        WaterPredictionRequestDto request = waterPredictionMapper.toRequestDto(
                temperature,
                humidity,
                telemetryData.light(),
                telemetryData.soilMoisture()
        );

        // we call ML serverless function to get predicted watering amount
        WaterPredictionResponseDto response = mlPredictionClient.predictWater(request);

        // we send the predicted watering amount back to IoT through MQTT

        publishWaterCommand(
                telemetryData.setupId(),
                response.wateringAmount()
        );
    }

    // method to send the actuator command to the mqtt publisher
    private void publishWaterCommand(int setupId, int amountMl) {
        String topic = "farm/" + setupId + "/cmd"; // topic for backend to IoT command

        // we create actuator command for water pump
        ActuatorCommandDto command = new ActuatorCommandDto(
                "water_pump",
                amountMl
        );



        try {
            // we convert actuator command DTO to JSON payload
            String payload = objectMapper.writeValueAsString(command);

            System.out.println("Publishing MQTT command to topic: " + topic);
            System.out.println("Payload: " + payload);

            // we publish the command to IoT over MQTT
            mqttPublisher.publish(topic, payload);
        } catch (Exception e) {
            throw new IllegalStateException("Could not publish actuator command", e);
        }
    }

    // helping method to convert soil moisture from raw ADC value to percentage
    private double convertSoilMoistureToPercent(int rawSoilMoisture) {
        return (rawSoilMoisture / 1023.0) * 100.0;
    }
}