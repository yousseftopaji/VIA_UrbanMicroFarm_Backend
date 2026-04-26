package dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.MqttTelemetryDataDto;
import dk.via.group1.urbanmicrofarm_backend.logic.services.SensorReadingService;
import dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper.MqttSensorReadingMapper;
import dk.via.group1.urbanmicrofarm_backend.mqtt.parser.MqttTelemetryDataParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("SensorReadingMqttMessageHandler")
@ExtendWith(MockitoExtension.class)
class SensorReadingMqttMessageHandlerTest {

  @Mock
  private MqttTelemetryDataParser telemetryDataParser;

  @Mock
  private MqttSensorReadingMapper sensorReadingMapper;

  @Mock
  private SensorReadingService sensorReadingService;

  @Captor
  private ArgumentCaptor<TelemetryData> telemetryDataCaptor;

  private SensorReadingMqttMessageHandler handler;

  @BeforeEach
  void setUp() {
    handler = new SensorReadingMqttMessageHandler(
        telemetryDataParser,
        sensorReadingMapper,
        sensorReadingService
    );
  }

  // ═══════════════════════════════════════════════════════════════
  // Z – ZERO (null, empty, zero values)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("should log error when payload is null")
  void should_logError_whenPayloadIsNull() {
    String topic = "sensors/telemetry";

    handler.handle(topic, null);

    verify(telemetryDataParser).fromJson(null);
    verifyNoInteractions(sensorReadingMapper, sensorReadingService);
  }

  @Test
  @DisplayName("should log error when payload is empty string")
  void should_logError_whenPayloadIsEmptyString() {
    String topic = "sensors/telemetry";
    when(telemetryDataParser.fromJson("")).thenThrow(new RuntimeException("Empty payload"));

    handler.handle(topic, "");

    verify(telemetryDataParser).fromJson("");
    verifyNoInteractions(sensorReadingMapper, sensorReadingService);
  }

  @Test
  @DisplayName("should log error when topic is null")
  void should_logError_whenTopicIsNull() {
    String payload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25}";

    when(telemetryDataParser.fromJson(payload)).thenThrow(new RuntimeException("Parse error"));

    handler.handle(null, payload);

    verify(telemetryDataParser).fromJson(payload);
  }

  @Test
  @DisplayName("should process reading when all sensor values are zero")
  void should_processReading_whenAllSensorValuesAreZero() {
    String topic = "sensors/telemetry";
    String payload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":0,\"humidity\":0,\"light\":0,\"soil_moisture\":0}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 100, 0, 0, 0, 0);
    TelemetryData domain = new TelemetryData(1, 100, 0, 0, 0, 0);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenReturn(domain);

    handler.handle(topic, payload);

    verify(sensorReadingService).processReadings(domain);
  }

  // ═══════════════════════════════════════════════════════════════
  // O – ONE (single valid message)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("should parse map and process single valid telemetry message")
  void should_parseMapAndProcessSingleValidTelemetryMessage() {
    String topic = "sensors/telemetry";
    String payload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25,\"humidity\":60,\"light\":500,\"soil_moisture\":45}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 100, 25, 60, 500, 45);
    TelemetryData domain = new TelemetryData(1, 100, 25, 60, 500, 45);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenReturn(domain);

    handler.handle(topic, payload);

    verify(telemetryDataParser).fromJson(payload);
    verify(sensorReadingMapper).fromPayload(dto);
    verify(sensorReadingService).processReadings(telemetryDataCaptor.capture());

    TelemetryData captured = telemetryDataCaptor.getValue();
    assertThat(captured.setupId()).isEqualTo(1);
    assertThat(captured.sensorId()).isEqualTo(100);
    assertThat(captured.temperature()).isEqualTo(25);
    assertThat(captured.humidity()).isEqualTo(60);
    assertThat(captured.light()).isEqualTo(500);
    assertThat(captured.soilMoisture()).isEqualTo(45);
  }

  // ═══════════════════════════════════════════════════════════════
  // M – MANY (multiple messages, various topics, complex payloads)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("should process multiple consecutive messages without state pollution")
  void should_processMultipleConsecutiveMessages_withoutStatePollution() {
    String topic = "sensors/telemetry";
    String payload1 = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25,\"humidity\":60,\"light\":500,\"soil_moisture\":45}";
    String payload2 = "{\"setup_id\":2,\"sensor_id\":200,\"temperature\":30,\"humidity\":70,\"light\":600,\"soil_moisture\":50}";

    MqttTelemetryDataDto dto1 = new MqttTelemetryDataDto(1, 100, 25, 60, 500, 45);
    MqttTelemetryDataDto dto2 = new MqttTelemetryDataDto(2, 200, 30, 70, 600, 50);
    TelemetryData domain1 = new TelemetryData(1, 100, 25, 60, 500, 45);
    TelemetryData domain2 = new TelemetryData(2, 200, 30, 70, 600, 50);

    when(telemetryDataParser.fromJson(payload1)).thenReturn(dto1);
    when(telemetryDataParser.fromJson(payload2)).thenReturn(dto2);
    when(sensorReadingMapper.fromPayload(dto1)).thenReturn(domain1);
    when(sensorReadingMapper.fromPayload(dto2)).thenReturn(domain2);

    handler.handle(topic, payload1);
    handler.handle(topic, payload2);

    verify(sensorReadingService).processReadings(domain1);
    verify(sensorReadingService).processReadings(domain2);
  }

  @Test
  @DisplayName("should process message when topic contains multiple path segments")
  void should_processMessage_whenTopicContainsMultiplePathSegments() {
    String topic = "farm/sector1/greenhouse/sensors/telemetry";
    String payload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25,\"humidity\":60,\"light\":500,\"soil_moisture\":45}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 100, 25, 60, 500, 45);
    TelemetryData domain = new TelemetryData(1, 100, 25, 60, 500, 45);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenReturn(domain);

    handler.handle(topic, payload);

    verify(sensorReadingService).processReadings(domain);
  }

  // ═══════════════════════════════════════════════════════════════
  // B – BOUNDARY (edge cases in payload structure)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("should process message when payload contains extra JSON fields")
  void should_processMessage_whenPayloadContainsExtraJsonFields() {
    String topic = "sensors/telemetry";
    String payload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25,\"humidity\":60,\"light\":500,\"soil_moisture\":45,\"extra_field\":\"ignored\"}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 100, 25, 60, 500, 45);
    TelemetryData domain = new TelemetryData(1, 100, 25, 60, 500, 45);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenReturn(domain);

    handler.handle(topic, payload);

    verify(sensorReadingService).processReadings(domain);
  }

  @Test
  @DisplayName("should process message when payload contains minimum required fields")
  void should_processMessage_whenPayloadContainsMinimumRequiredFields() {
    String topic = "sensors/telemetry";
    String payload = "{\"setup_id\":1,\"sensor_id\":1,\"temperature\":0,\"humidity\":0,\"light\":0,\"soil_moisture\":0}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 1, 0, 0, 0, 0);
    TelemetryData domain = new TelemetryData(1, 1, 0, 0, 0, 0);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenReturn(domain);

    handler.handle(topic, payload);

    verify(sensorReadingService).processReadings(domain);
  }

  // ═══════════════════════════════════════════════════════════════
  // I – INTERFACE (verify interaction between collaborators)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("should invoke parser mapper and service in correct order when message is valid")
  void should_invokeParserMapperAndServiceInCorrectOrder_whenMessageIsValid() {
    String topic = "sensors/telemetry";
    String payload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25,\"humidity\":60,\"light\":500,\"soil_moisture\":45}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 100, 25, 60, 500, 45);
    TelemetryData domain = new TelemetryData(1, 100, 25, 60, 500, 45);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenReturn(domain);

    handler.handle(topic, payload);

    var inOrder = inOrder(telemetryDataParser, sensorReadingMapper, sensorReadingService);
    inOrder.verify(telemetryDataParser).fromJson(payload);
    inOrder.verify(sensorReadingMapper).fromPayload(dto);
    inOrder.verify(sensorReadingService).processReadings(domain);
  }

  @Test
  @DisplayName("should not invoke service when mapper throws exception")
  void should_notInvokeService_whenMapperThrowsException() {
    String topic = "sensors/telemetry";
    String payload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 100, 25, 60, 500, 45);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenThrow(new IllegalArgumentException("Invalid data"));

    handler.handle(topic, payload);

    verify(telemetryDataParser).fromJson(payload);
    verify(sensorReadingMapper).fromPayload(dto);
    verifyNoInteractions(sensorReadingService);
  }

  @Test
  @DisplayName("should not invoke mapper or service when parser throws exception")
  void should_notInvokeMapperOrService_whenParserThrowsException() {
    String topic = "sensors/telemetry";
    String payload = "invalid-json{";

    when(telemetryDataParser.fromJson(payload)).thenThrow(new RuntimeException("JSON parse error"));

    handler.handle(topic, payload);

    verify(telemetryDataParser).fromJson(payload);
    verifyNoInteractions(sensorReadingMapper, sensorReadingService);
  }

  // ═══════════════════════════════════════════════════════════════
  // E – EXCEPTION (error handling and failure modes)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("should catch and log exception when parser throws runtime exception")
  void should_catchAndLogException_whenParserThrowsRuntimeException() {
    String topic = "sensors/telemetry";
    String payload = "malformed";
    RuntimeException parseError = new RuntimeException("Unexpected token");

    when(telemetryDataParser.fromJson(payload)).thenThrow(parseError);

    handler.handle(topic, payload);

    verify(telemetryDataParser).fromJson(payload);
    verifyNoInteractions(sensorReadingMapper, sensorReadingService);
    // Exception is caught and logged — handler does not propagate
  }

  @Test
  @DisplayName("should catch and log exception when mapper throws illegal argument exception")
  void should_catchAndLogException_whenMapperThrowsIllegalArgumentException() {
    String topic = "sensors/telemetry";
    String payload = "{\"setup_id\":0,\"sensor_id\":null}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(0, null, 0, 0, 0, 0);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenThrow(new IllegalArgumentException("setupId must be > 0"));

    handler.handle(topic, payload);

    verify(telemetryDataParser).fromJson(payload);
    verify(sensorReadingMapper).fromPayload(dto);
    verifyNoInteractions(sensorReadingService);
  }

  @Test
  @DisplayName("should catch and log exception when service throws runtime exception")
  void should_catchAndLogException_whenServiceThrowsRuntimeException() {
    String topic = "sensors/telemetry";
    String payload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25,\"humidity\":60,\"light\":500,\"soil_moisture\":45}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 100, 25, 60, 500, 45);
    TelemetryData domain = new TelemetryData(1, 100, 25, 60, 500, 45);

    when(telemetryDataParser.fromJson(payload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenReturn(domain);
    doThrow(new RuntimeException("Database unavailable")).when(sensorReadingService).processReadings(domain);

    handler.handle(topic, payload);

    verify(telemetryDataParser).fromJson(payload);
    verify(sensorReadingMapper).fromPayload(dto);
    verify(sensorReadingService).processReadings(domain);
    // Exception is caught and logged — handler does not propagate
  }

  @Test
  @DisplayName("should continue operating after previous message caused exception")
  void should_continueOperating_afterPreviousMessageCausedException() {
    String topic = "sensors/telemetry";
    String badPayload = "invalid";
    String goodPayload = "{\"setup_id\":1,\"sensor_id\":100,\"temperature\":25,\"humidity\":60,\"light\":500,\"soil_moisture\":45}";
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(1, 100, 25, 60, 500, 45);
    TelemetryData domain = new TelemetryData(1, 100, 25, 60, 500, 45);

    when(telemetryDataParser.fromJson(badPayload)).thenThrow(new RuntimeException("Parse error"));
    when(telemetryDataParser.fromJson(goodPayload)).thenReturn(dto);
    when(sensorReadingMapper.fromPayload(dto)).thenReturn(domain);

    handler.handle(topic, badPayload);
    handler.handle(topic, goodPayload);

    verify(sensorReadingService).processReadings(domain);
  }
}