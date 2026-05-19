package dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper;

import dk.via.group1.urbanmicrofarm_backend.dto.TelemetryData;
import dk.via.group1.urbanmicrofarm_backend.dto.mqttDto.MqttTelemetryDataDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MqttSensorReadingMapper")
class MqttSensorReadingMapperTest {

  private final MqttSensorReadingMapper mapper = new MqttSensorReadingMapper();

  @Test
  @DisplayName("should throw IllegalArgumentException when fromPayload receives null")
  void should_throwIllegalArgumentException_whenFromPayloadReceivesNull() {
    assertThatThrownBy(() -> mapper.fromPayload(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  @DisplayName("should throw IllegalArgumentException when toPayload receives null")
  void should_throwIllegalArgumentException_whenToPayloadReceivesNull() {
    assertThatThrownBy(() -> mapper.toPayload(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must not be null");
  }

  @Test
  @DisplayName("should throw when serialNumber is blank or null")
  void should_throw_whenSerialNumberInvalid() {
    MqttTelemetryDataDto dtoBlank = new MqttTelemetryDataDto("", 1, 20, 50, 300, 40);
    MqttTelemetryDataDto dtoNull = new MqttTelemetryDataDto(null, 1, 20, 50, 300, 40);

    assertThatThrownBy(() -> mapper.fromPayload(dtoBlank))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("serialNumber");

    assertThatThrownBy(() -> mapper.fromPayload(dtoNull))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("serialNumber");
  }

  @Test
  @DisplayName("should throw IllegalArgumentException when sensorId is null")
  void should_throwIllegalArgumentException_whenSensorIdIsNull() {
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto("SN-1", null, 20, 50, 300, 40);

    assertThatThrownBy(() -> mapper.fromPayload(dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("sensorId must not be null");
  }

  @Test
  @DisplayName("should map DTO to domain entity when all fields are valid")
  void should_mapDtoToDomainEntity_whenAllFieldsAreValid() {
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto("SN-1", 100, 25, 60, 500, 45);

    TelemetryData result = mapper.fromPayload(dto);

    assertThat(result.serialNumber()).isEqualTo("SN-1");
    assertThat(result.sensorId()).isEqualTo(100);
    assertThat(result.temperature()).isEqualTo(25);
    assertThat(result.humidity()).isEqualTo(60);
    assertThat(result.light()).isEqualTo(500);
    assertThat(result.soilMoisture()).isEqualTo(45);
  }

  @Test
  @DisplayName("should map domain entity back to DTO when all fields are valid")
  void should_mapDomainEntityBackToDto_whenAllFieldsAreValid() {
    TelemetryData domain = new TelemetryData("SN-1", 100, 25, 60, 500, 45);

    MqttTelemetryDataDto result = mapper.toPayload(domain);

    assertThat(result.serialNumber()).isEqualTo("SN-1");
    assertThat(result.sensorId()).isEqualTo(100);
    assertThat(result.temperature()).isEqualTo(25);
    assertThat(result.humidity()).isEqualTo(60);
    assertThat(result.light()).isEqualTo(500);
    assertThat(result.soilMoisture()).isEqualTo(45);
  }

  @ParameterizedTest(name = "should map DTO to domain entity when readings are [{0}, {1}, {2}, {3}]")
  @CsvSource({
      "0, 0, 0, 0",      // all zero readings
      "-10, 0, 100, 50", // negative temperature
      "50, 100, 1000, 100" // max-ish values
  })
  @DisplayName("should map DTO to domain entity for various sensor reading combinations")
  void should_mapDtoToDomainEntity_forVariousSensorReadingCombinations(
      int temperature, int humidity, int light, int soilMoisture) {

    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(
        "SN-1", 1, temperature, humidity, light, soilMoisture
    );

    TelemetryData result = mapper.fromPayload(dto);

    assertThat(result.temperature()).isEqualTo(temperature);
    assertThat(result.humidity()).isEqualTo(humidity);
    assertThat(result.light()).isEqualTo(light);
    assertThat(result.soilMoisture()).isEqualTo(soilMoisture);
  }

  @Test
  @DisplayName("should map DTO to domain entity when serialNumber is present")
  void should_mapDtoToDomainEntity_whenSerialNumberIsPresent() {
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto("SN-1", 1, 20, 50, 300, 40);

    TelemetryData result = mapper.fromPayload(dto);

    assertThat(result.serialNumber()).isEqualTo("SN-1");
  }

  @Test
  @DisplayName("should preserve sensorId when it is at maximum integer value")
  void should_preserveSensorId_whenItIsAtMaximumIntegerValue() {
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(
        "SN-1", Integer.MAX_VALUE, 20, 50, 300, 40
    );

    TelemetryData result = mapper.fromPayload(dto);

    assertThat(result.sensorId()).isEqualTo(Integer.MAX_VALUE);
  }

  @Test
  @DisplayName("should preserve all data when mapping DTO to domain and back to DTO")
  void should_preserveAllData_whenMappingDtoToDomainAndBackToDto() {
    MqttTelemetryDataDto original = new MqttTelemetryDataDto("SN-42", 99, 23, 55, 400, 30);

    TelemetryData domain = mapper.fromPayload(original);
    MqttTelemetryDataDto roundTripped = mapper.toPayload(domain);

    assertThat(roundTripped.serialNumber()).isEqualTo(original.serialNumber());
    assertThat(roundTripped.sensorId()).isEqualTo(original.sensorId());
    assertThat(roundTripped.temperature()).isEqualTo(original.temperature());
    assertThat(roundTripped.humidity()).isEqualTo(original.humidity());
    assertThat(roundTripped.light()).isEqualTo(original.light());
    assertThat(roundTripped.soilMoisture()).isEqualTo(original.soilMoisture());
  }

  @ParameterizedTest(name = "should throw IllegalArgumentException when serialNumber is invalid: {0}")
  @ValueSource(strings = {"", "   "})
  @DisplayName("should throw IllegalArgumentException for all invalid serialNumber values")
  void should_throwIllegalArgumentException_forAllInvalidSerialNumberValues(String invalidSerial) {
    MqttTelemetryDataDto dto = new MqttTelemetryDataDto(
        invalidSerial, 1, 20, 50, 300, 40
    );

    assertThatThrownBy(() -> mapper.fromPayload(dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("serialNumber");
  }
}