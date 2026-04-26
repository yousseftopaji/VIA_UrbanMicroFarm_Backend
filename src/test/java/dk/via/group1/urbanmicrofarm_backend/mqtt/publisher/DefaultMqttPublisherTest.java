package dk.via.group1.urbanmicrofarm_backend.mqtt.publisher;

import dk.via.group1.urbanmicrofarm_backend.mqtt.client.MqttClientFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("DefaultMqttPublisher")
@ExtendWith(MockitoExtension.class)
class DefaultMqttPublisherTest {

  @Mock
  private MqttClientFactory clientFactory;

  @Mock
  private MqttClient mqttClient;

  @Captor
  private ArgumentCaptor<MqttMessage> messageCaptor;

  private DefaultMqttPublisher publisher;

  @BeforeEach
  void setUp() {
    publisher = new DefaultMqttPublisher(clientFactory);
  }

  // ═══════════════════════════════════════════════════════════════
  // Z – ZERO (null, empty values)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("publish_nullTopic_illegalArgumentThrown")
  void publish_nullTopic_illegalArgumentThrown() throws MqttException {

    assertThatThrownBy(() -> publisher.publish(null, "some-payload"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("topic");
  }

  @Test
  @DisplayName("publish_nullPayload_messagePublishedWithEmptyBody")
  void publish_nullPayload_messagePublishedWithEmptyBody() throws MqttException {
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);

    publisher.publish("sensors/telemetry", null);

    verify(mqttClient).publish(anyString(), messageCaptor.capture());
    MqttMessage captured = messageCaptor.getValue();
    assertThat(captured.getPayload()).isEqualTo(new byte[0]);
    assertThat(captured.getQos()).isEqualTo(1);
  }

  @Test
  @DisplayName("publish_emptyPayload_messagePublishedWithEmptyBody")
  void publish_emptyPayload_messagePublishedWithEmptyBody() throws MqttException {
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);

    publisher.publish("sensors/telemetry", "");

    verify(mqttClient).publish(anyString(), messageCaptor.capture());
    MqttMessage captured = messageCaptor.getValue();
    assertThat(captured.getPayload()).isEqualTo(new byte[0]);
  }

  // ═══════════════════════════════════════════════════════════════
  // O – ONE (single valid publish)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("publish_validTopicAndPayload_messagePublishedWithQos1")
  void publish_validTopicAndPayload_messagePublishedWithQos1() throws MqttException {
    String topic = "sensors/telemetry";
    String payload = "{\"temperature\":25}";
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);

    publisher.publish(topic, payload);

    verify(mqttClient).publish(eq(topic), messageCaptor.capture());
    MqttMessage captured = messageCaptor.getValue();
    assertThat(new String(captured.getPayload(), StandardCharsets.UTF_8)).isEqualTo(payload);
    assertThat(captured.getQos()).isEqualTo(1);
  }

  // ═══════════════════════════════════════════════════════════════
  // M – MANY (multiple publishes, various topics/payloads)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("publish_multipleCallsEachWithNewClient_factoryCreatesNewClientEachTime")
  void publish_multipleCallsEachWithNewClient_factoryCreatesNewClientEachTime() throws MqttException {
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    publisher.publish("topic/1", "payload-1");
    publisher.publish("topic/2", "payload-2");
    publisher.publish("topic/3", "payload-3");

    verify(clientFactory, times(3)).createPublisherClient();
    verify(mqttClient, times(3)).publish(anyString(), any(MqttMessage.class));
    verify(mqttClient, times(3)).disconnect();
    verify(mqttClient, times(3)).close();
  }

  @Test
  @DisplayName("publish_largePayload_messagePublishedWithCorrectBytes")
  void publish_largePayload_messagePublishedWithCorrectBytes() throws MqttException {
    String largePayload = "x".repeat(10000);
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);

    publisher.publish("sensors/bulk", largePayload);

    verify(mqttClient).publish(anyString(), messageCaptor.capture());
    assertThat(new String(messageCaptor.getValue().getPayload(), StandardCharsets.UTF_8))
        .isEqualTo(largePayload);
  }

  // ═══════════════════════════════════════════════════════════════
  // B – BOUNDARY (edge cases)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("publish_unicodePayload_messagePublishedWithUtf8Encoding")
  void publish_unicodePayload_messagePublishedWithUtf8Encoding() throws MqttException {
    String unicodePayload = "温度: 25°C, 湿度: 60%";
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);

    publisher.publish("sensors/telemetry", unicodePayload);

    verify(mqttClient).publish(anyString(), messageCaptor.capture());
    assertThat(new String(messageCaptor.getValue().getPayload(), StandardCharsets.UTF_8))
        .isEqualTo(unicodePayload);
  }

  @Test
  @DisplayName("publish_topicWithSpecialChars_messagePublishedToExactTopic")
  void publish_topicWithSpecialChars_messagePublishedToExactTopic() throws MqttException {
    String specialTopic = "farm/sector-1/greenhouse_2/sensors/+";
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);

    publisher.publish(specialTopic, "data");

    verify(mqttClient).publish(eq(specialTopic), any(MqttMessage.class));
  }

  // ═══════════════════════════════════════════════════════════════
  // I – INTERFACE (verify interactions with collaborators)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("publish_validMessage_clientDisconnectedAndClosedAfterPublish")
  void publish_validMessage_clientDisconnectedAndClosedAfterPublish() throws MqttException {
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    publisher.publish("sensors/telemetry", "data");

    var inOrder = inOrder(mqttClient);
    inOrder.verify(mqttClient).publish(anyString(), any(MqttMessage.class));
    inOrder.verify(mqttClient).disconnect();
    inOrder.verify(mqttClient).close();
  }

  @Test
  @DisplayName("publish_clientNotConnected_disconnectNotCalled")
  void publish_clientNotConnected_disconnectNotCalled() throws MqttException {
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(false);

    publisher.publish("sensors/telemetry", "data");

    verify(mqttClient).publish(anyString(), any(MqttMessage.class));
    verify(mqttClient, never()).disconnect();
    verify(mqttClient).close();
  }

  @Test
  @DisplayName("publish_validMessage_factoryCreatesPublisherClient")
  void publish_validMessage_factoryCreatesPublisherClient() throws MqttException {
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);

    publisher.publish("sensors/telemetry", "data");

    verify(clientFactory).createPublisherClient();
  }

  // ═══════════════════════════════════════════════════════════════
  // E – EXCEPTION (error handling and failure modes)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("publish_factoryThrowsMqttException_exceptionPropagated")
  void publish_factoryThrowsMqttException_exceptionPropagated() throws MqttException {
    when(clientFactory.createPublisherClient()).thenThrow(new MqttException(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED));

    assertThatThrownBy(() -> publisher.publish("sensors/telemetry", "data"))
        .isInstanceOf(MqttException.class)
        .satisfies(ex -> assertThat(((MqttException) ex).getReasonCode())
            .isEqualTo(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED));
  }

  @Test
  @DisplayName("publish_publishThrowsMqttException_clientStillClosed")
  void publish_publishThrowsMqttException_clientStillClosed() throws MqttException {
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);
    doThrow(new MqttException(MqttException.REASON_CODE_MAX_INFLIGHT)).when(mqttClient).publish(anyString(), any(MqttMessage.class));

    assertThatThrownBy(() -> publisher.publish("sensors/telemetry", "data"))
        .isInstanceOf(MqttException.class);

    verify(mqttClient).close();
  }

  @Test
  @DisplayName("publish_disconnectThrowsException_closeStillCalled")
  void publish_disconnectThrowsException_closeStillCalled() throws MqttException {
    when(clientFactory.createPublisherClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);
    doThrow(new RuntimeException("Disconnect failed")).when(mqttClient).disconnect();

    assertThatThrownBy(() -> publisher.publish("sensors/telemetry", "data"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Disconnect failed");

    verify(mqttClient).close();
  }
}