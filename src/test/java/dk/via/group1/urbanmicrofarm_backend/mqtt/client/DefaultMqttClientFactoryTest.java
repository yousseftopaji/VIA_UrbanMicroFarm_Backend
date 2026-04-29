package dk.via.group1.urbanmicrofarm_backend.mqtt.client;

import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("DefaultMqttClientFactory")
@ExtendWith(MockitoExtension.class)
class DefaultMqttClientFactoryTest {

  @Mock
  private MqttClientConfig config;

  @Captor
  private ArgumentCaptor<MqttConnectOptions> connectOptionsCaptor;

  private DefaultMqttClientFactory factory;

  @BeforeEach
  void setUp() {
    factory = new DefaultMqttClientFactory(config);
  }

  // ═══════════════════════════════════════════════════════════════
  // Z – ZERO (null, empty, missing config values)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("createSubscriberClient_nullBroker_illegalArgumentThrown")
  void createSubscriberClient_nullBroker_illegalArgumentThrown() {
    when(config.getBroker()).thenReturn(null);

    assertThatThrownBy(() -> factory.createSubscriberClient())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("broker must not be null or blank");
  }

  @Test
  @DisplayName("createSubscriberClient_nullClientId_illegalArgumentThrown")
  void createSubscriberClient_nullClientId_illegalArgumentThrown() {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn(null);

    assertThatThrownBy(() -> factory.createSubscriberClient())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("clientId must not be null or blank");
  }

  @Test
  @DisplayName("createPublisherClient_nullBroker_illegalArgumentThrown")
  void createPublisherClient_nullBroker_illegalArgumentThrown() {
    when(config.getBroker()).thenReturn(null);

    assertThatThrownBy(() -> factory.createPublisherClient())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("broker must not be null or blank");
  }

  @Test
  @DisplayName("createPublisherClient_nullClientId_illegalArgumentThrown")
  void createPublisherClient_nullClientId_illegalArgumentThrown() {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn(null);

    assertThatThrownBy(() -> factory.createPublisherClient())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("clientId must not be null or blank");
  }

  @Test
  @DisplayName("createSubscriberClient_emptyBroker_illegalArgumentThrown")
  void createSubscriberClient_emptyBroker_illegalArgumentThrown() {
    when(config.getBroker()).thenReturn("");

    assertThatThrownBy(() -> factory.createSubscriberClient())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("broker must not be null or blank");
  }

  @Test
  @DisplayName("createSubscriberClient_blankBroker_illegalArgumentThrown")
  void createSubscriberClient_blankBroker_illegalArgumentThrown() {
    when(config.getBroker()).thenReturn("   ");

    assertThatThrownBy(() -> factory.createSubscriberClient())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("broker must not be null or blank");
  }

  @Test
  @DisplayName("createSubscriberClient_emptyClientId_illegalArgumentThrown")
  void createSubscriberClient_emptyClientId_illegalArgumentThrown() {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("");

    assertThatThrownBy(() -> factory.createSubscriberClient())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("clientId must not be null or blank");
  }

  // ═══════════════════════════════════════════════════════════════
  // O – ONE (single valid configuration)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("createSubscriberClient_validConfig_connectedClientReturned")
  void createSubscriberClient_validConfig_connectedClientReturned() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("farm-subscriber");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      MqttClient client = factory.createSubscriberClient();

      assertThat(client).isNotNull();
      verify(client).connect(any(MqttConnectOptions.class));
    }
  }

  @Test
  @DisplayName("createPublisherClient_validConfig_connectedClientReturned")
  void createPublisherClient_validConfig_connectedClientReturned() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("farm-publisher");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      MqttClient client = factory.createPublisherClient();

      assertThat(client).isNotNull();
      verify(client).connect(any(MqttConnectOptions.class));
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // M – MANY (various valid configurations)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("createSubscriberClient_sslBroker_connectedClientReturned")
  void createSubscriberClient_sslBroker_connectedClientReturned() throws MqttException {
    when(config.getBroker()).thenReturn("ssl://secure-broker.example.com:8883");
    when(config.getClientId()).thenReturn("ssl-subscriber");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      MqttClient client = factory.createSubscriberClient();

      assertThat(client).isNotNull();
      verify(client).connect(any(MqttConnectOptions.class));
    }
  }

  @Test
  @DisplayName("createPublisherClient_wsBroker_connectedClientReturned")
  void createPublisherClient_wsBroker_connectedClientReturned() throws MqttException {
    when(config.getBroker()).thenReturn("ws://broker.example.com:8080/mqtt");
    when(config.getClientId()).thenReturn("ws-publisher");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      MqttClient client = factory.createPublisherClient();

      assertThat(client).isNotNull();
      verify(client).connect(any(MqttConnectOptions.class));
    }
  }

  @Test
  @DisplayName("createSubscriberClient_longClientId_connectedClientReturned")
  void createSubscriberClient_longClientId_connectedClientReturned() throws MqttException {
    String longClientId = "urban-micro-farm-backend-subscriber-instance-001-production-eu-west";

    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn(longClientId);

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      MqttClient client = factory.createSubscriberClient();

      assertThat(client).isNotNull();
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // B – BOUNDARY (edge cases for config values)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("createSubscriberClient_nonStandardPort_connectedClientReturned")
  void createSubscriberClient_nonStandardPort_connectedClientReturned() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:9999");
    when(config.getClientId()).thenReturn("custom-port-client");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      MqttClient client = factory.createSubscriberClient();

      assertThat(client).isNotNull();
    }
  }

  @Test
  @DisplayName("createSubscriberClient_ipv6Broker_connectedClientReturned")
  void createSubscriberClient_ipv6Broker_connectedClientReturned() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://[::1]:1883");
    when(config.getClientId()).thenReturn("ipv6-client");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      MqttClient client = factory.createSubscriberClient();

      assertThat(client).isNotNull();
    }
  }

  @Test
  @DisplayName("createPublisherClient_shortClientId_connectedClientReturned")
  void createPublisherClient_shortClientId_connectedClientReturned() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("a");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      MqttClient client = factory.createPublisherClient();

      assertThat(client).isNotNull();
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // I – INTERFACE (verify interaction with MqttClient and options)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("createPublisherClient_validConfig_publisherSuffixAppendedToClientId")
  void createPublisherClient_validConfig_publisherSuffixAppendedToClientId() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("farm-client");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class,
        (mock, context) -> {
          assertThat(context.arguments()).hasSize(2);
          assertThat(context.arguments().get(0)).isEqualTo("tcp://localhost:1883");
          assertThat(context.arguments().get(1)).isEqualTo("farm-client-publisher");
        })) {

      factory.createPublisherClient();
    }
  }

  @Test
  @DisplayName("createSubscriberClient_validConfig_cleanSessionFalseSetInOptions")
  void createSubscriberClient_validConfig_cleanSessionFalseSetInOptions() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("subscriber-options-test");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      factory.createSubscriberClient();

      MqttClient client = mocked.constructed().get(0);
      verify(client).connect(connectOptionsCaptor.capture());

      MqttConnectOptions captured = connectOptionsCaptor.getValue();
      assertThat(captured.isCleanSession()).isFalse();
      assertThat(captured.isAutomaticReconnect()).isTrue();
      assertThat(captured.getConnectionTimeout()).isEqualTo(10);
    }
  }

  @Test
  @DisplayName("createPublisherClient_validConfig_cleanSessionTrueSetInOptions")
  void createPublisherClient_validConfig_cleanSessionTrueSetInOptions() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("publisher-options-test");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class)) {
      factory.createPublisherClient();

      MqttClient client = mocked.constructed().get(0);
      verify(client).connect(connectOptionsCaptor.capture());

      MqttConnectOptions captured = connectOptionsCaptor.getValue();
      assertThat(captured.isCleanSession()).isTrue();
      assertThat(captured.isAutomaticReconnect()).isTrue();
      assertThat(captured.getConnectionTimeout()).isEqualTo(10);
    }
  }

  // ═══════════════════════════════════════════════════════════════
  // E – EXCEPTION (error paths and failure modes)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("createSubscriberClient_connectThrowsMqttException_mqttExceptionPropagated")
  void createSubscriberClient_connectThrowsMqttException_mqttExceptionPropagated() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("failing-client");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class,
        (mock, context) -> doThrow(new MqttException(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED))
            .when(mock).connect(any(MqttConnectOptions.class)))) {

      assertThatThrownBy(() -> factory.createSubscriberClient())
          .isInstanceOf(MqttException.class)
          .satisfies(ex -> assertThat(((MqttException) ex).getReasonCode())
              .isEqualTo(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED));
    }
  }

  @Test
  @DisplayName("createPublisherClient_connectThrowsMqttException_mqttExceptionPropagated")
  void createPublisherClient_connectThrowsMqttException_mqttExceptionPropagated() throws MqttException {
    when(config.getBroker()).thenReturn("tcp://localhost:1883");
    when(config.getClientId()).thenReturn("failing-publisher");

    try (MockedConstruction<MqttClient> mocked = mockConstruction(MqttClient.class,
        (mock, context) -> doThrow(new MqttException(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED))
            .when(mock).connect(any(MqttConnectOptions.class)))) {

      assertThatThrownBy(() -> factory.createPublisherClient())
          .isInstanceOf(MqttException.class);
    }
  }
}