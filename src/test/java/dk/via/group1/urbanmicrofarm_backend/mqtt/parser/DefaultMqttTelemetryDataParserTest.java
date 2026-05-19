package dk.via.group1.urbanmicrofarm_backend.mqtt.parser;

import tools.jackson.databind.ObjectMapper;
import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.MqttTelemetryDataDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultMqttTelemetryDataParserTest
{

  private final MqttTelemetryDataParser parser = new DefaultMqttTelemetryDataParser(new ObjectMapper());

  @Test
  void fromJson_shouldMapSnakeCaseJsonToDto() {
    String json = """
        {
          "serial_number": "SN123",
          "sensor_id": 3,
          "temperature": 24,
          "humidity": 51,
          "light": 900,
          "soil_moisture": 18
        }
        """;

    MqttTelemetryDataDto dto = parser.fromJson(json);

    assertEquals("SN123", dto.serialNumber());
    assertEquals(3, dto.sensorId());
    assertEquals(24, dto.temperature());
    assertEquals(51, dto.humidity());
    assertEquals(900, dto.light());
    assertEquals(18, dto.soilMoisture());
  }

  @Test
  void toJson_shouldSerializeDtoToSnakeCaseJson() {
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto("SN123", 3, 24, 51, 900, 18);

    String json = parser.toJson(dto);

    assertEquals("{\"serial_number\":\"SN123\",\"sensor_id\":3,\"temperature\":24,\"humidity\":51,\"light\":900,\"soil_moisture\":18}", json);
  }

  @Test
  void fromJson_shouldRejectBlankPayload() {
    assertThrows(IllegalArgumentException.class, () -> parser.fromJson("   "));
  }
}

