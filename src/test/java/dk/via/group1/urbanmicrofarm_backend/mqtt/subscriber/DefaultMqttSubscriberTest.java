package dk.via.group1.urbanmicrofarm_backend.mqtt.subscriber;

import dk.via.group1.urbanmicrofarm_backend.mqtt.client.MqttClientFactory;
import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler.MqttMessageHandler;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
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
import org.springframework.boot.ApplicationArguments;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("DefaultMqttSubscriber")
@ExtendWith(MockitoExtension.class)
class DefaultMqttSubscriberTest {

  @Mock
  private MqttClientConfig config;

  @Mock
  private MqttClientFactory clientFactory;

  @Mock
  private MqttMessageHandler messageHandler;

  @Mock
  private MqttClient mqttClient;

  @Mock
  private ApplicationArguments applicationArguments;

  @Captor
  private ArgumentCaptor<IMqttMessageListener> listenerCaptor;

  private DefaultMqttSubscriber subscriber;

  @BeforeEach
  void setUp() {
    subscriber = new DefaultMqttSubscriber(config, clientFactory, messageHandler);
  }

  // ═══════════════════════════════════════════════════════════════
  // Z – ZERO (null, empty, uninitialized state)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("subscribe_clientNotStarted_illegalStateThrown")
  void subscribe_clientNotStarted_illegalStateThrown() {
    assertThatThrownBy(() -> subscriber.subscribe("sensors/telemetry"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("not connected");
  }

  @Test
  @DisplayName("stop_clientNeverStarted_noExceptionThrown")
  void stop_clientNeverStarted_noExceptionThrown() throws MqttException {
    subscriber.stop();

    verifyNoInteractions(mqttClient);
  }

  // ═══════════════════════════════════════════════════════════════
  // O – ONE (single valid operation)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("start_validConfig_clientSubscribesToConfiguredTopic")
  void start_validConfig_clientSubscribesToConfiguredTopic() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();

    verify(mqttClient).subscribe(eq("sensors/telemetry"), eq(1), any(IMqttMessageListener.class));
  }

  @Test
  @DisplayName("subscribe_afterStart_messageHandlerInvokedOnMessageArrival")
  void subscribe_afterStart_messageHandlerInvokedOnMessageArrival() throws Exception {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();
    subscriber.subscribe("sensors/telemetry");

    verify(mqttClient, times(2)).subscribe(eq("sensors/telemetry"), eq(1), listenerCaptor.capture());

    // Get the listener from the LAST subscribe call (the explicit one)
    IMqttMessageListener listener = listenerCaptor.getValue();
    MqttMessage mqttMessage = new MqttMessage("{\"temperature\":25}".getBytes(StandardCharsets.UTF_8));
    listener.messageArrived("sensors/telemetry", mqttMessage);

    verify(messageHandler).handle("sensors/telemetry", "{\"temperature\":25}");
  }

  // ═══════════════════════════════════════════════════════════════
  // M – MANY (multiple topics, multiple messages)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("subscribe_multipleTopicsAfterStart_eachSubscribedIndependently")
  void subscribe_multipleTopicsAfterStart_eachSubscribedIndependently() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();

    subscriber.subscribe("sensors/temperature");
    subscriber.subscribe("sensors/humidity");
    subscriber.subscribe("sensors/light");

    verify(mqttClient, times(4)).subscribe(anyString(), eq(1), any(IMqttMessageListener.class));
    // 1 from start() + 3 explicit calls
  }

  @Test
  @DisplayName("subscribe_multipleMessages_allHandledByMessageHandler")
  void subscribe_multipleMessages_allHandledByMessageHandler() throws Exception {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();
    subscriber.subscribe("sensors/telemetry");

    verify(mqttClient, times(2)).subscribe(anyString(), eq(1), listenerCaptor.capture());
    // Get the LAST listener captured (from the explicit subscribe call)
    IMqttMessageListener listener = listenerCaptor.getAllValues().get(1);

    listener.messageArrived("sensors/telemetry", new MqttMessage("msg1".getBytes()));
    listener.messageArrived("sensors/telemetry", new MqttMessage("msg2".getBytes()));
    listener.messageArrived("sensors/telemetry", new MqttMessage("msg3".getBytes()));

    verify(messageHandler, times(3)).handle(eq("sensors/telemetry"), anyString());
  }

  // ═══════════════════════════════════════════════════════════════
  // B – BOUNDARY (edge cases)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("subscribe_topicWithWildcardAfterStart_wildcardSubscriptionMade")
  void subscribe_topicWithWildcardAfterStart_wildcardSubscriptionMade() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();
    subscriber.subscribe("sensors/+/telemetry");

    verify(mqttClient).subscribe(eq("sensors/+/telemetry"), eq(1), any(IMqttMessageListener.class));
  }

  @Test
  @DisplayName("subscribe_topicWithMultiLevelWildcardAfterStart_subscriptionMade")
  void subscribe_topicWithMultiLevelWildcardAfterStart_subscriptionMade() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();
    subscriber.subscribe("farm/#");

    verify(mqttClient).subscribe(eq("farm/#"), eq(1), any(IMqttMessageListener.class));
  }

  @Test
  @DisplayName("subscribe_emptyPayloadMessageAfterStart_handlerReceivesEmptyString")
  void subscribe_emptyPayloadMessageAfterStart_handlerReceivesEmptyString() throws Exception {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();
    subscriber.subscribe("sensors/telemetry");

    verify(mqttClient, times(2)).subscribe(anyString(), eq(1), listenerCaptor.capture());
    IMqttMessageListener listener = listenerCaptor.getAllValues().get(1);

    listener.messageArrived("sensors/telemetry", new MqttMessage(new byte[0]));

    verify(messageHandler).handle("sensors/telemetry", "");
  }

  // ═══════════════════════════════════════════════════════════════
  // I – INTERFACE (verify interactions with collaborators)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("start_factoryCreatesSubscriberClient")
  void start_factoryCreatesSubscriberClient() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();

    verify(clientFactory).createSubscriberClient();
  }

  @Test
  @DisplayName("stop_connectedClient_clientDisconnectedAndClosed")
  void stop_connectedClient_clientDisconnectedAndClosed() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();
    subscriber.stop();

    var inOrder = inOrder(mqttClient);
    inOrder.verify(mqttClient).disconnect();
    inOrder.verify(mqttClient).close();
  }

  @Test
  @DisplayName("stop_disconnectedClient_closeStillCalled")
  void stop_disconnectedClient_closeStillCalled() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();

    when(mqttClient.isConnected()).thenReturn(false);
    subscriber.stop();

    verify(mqttClient, never()).disconnect();
    verify(mqttClient).close();
  }

  @Test
  @DisplayName("run_applicationArgumentsProvided_startCalled")
  void run_applicationArgumentsProvided_startCalled() throws Exception {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.run(applicationArguments);

    verify(mqttClient).subscribe(anyString(), eq(1), any(IMqttMessageListener.class));
  }

  // ═══════════════════════════════════════════════════════════════
  // E – EXCEPTION (error handling and failure modes)
  // ═══════════════════════════════════════════════════════════════

  @Test
  @DisplayName("start_factoryThrowsMqttException_exceptionPropagated")
  void start_factoryThrowsMqttException_exceptionPropagated() throws MqttException {
    when(clientFactory.createSubscriberClient()).thenThrow(
        new MqttException(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED));

    assertThatThrownBy(() -> subscriber.start())
        .isInstanceOf(MqttException.class);
  }

  @Test
  @DisplayName("subscribe_afterStartThenDisconnect_illegalStateThrown")
  void subscribe_afterStartThenDisconnect_illegalStateThrown() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();

    // Simulate disconnect
    when(mqttClient.isConnected()).thenReturn(false);

    assertThatThrownBy(() -> subscriber.subscribe("sensors/telemetry"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("not connected");
  }

  @Test
  @DisplayName("stop_disconnectThrowsException_closeStillCalled")
  void stop_disconnectThrowsException_closeStillCalled() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);

    subscriber.start();

    doThrow(new RuntimeException("Disconnect failed")).when(mqttClient).disconnect();

    assertThatThrownBy(() -> subscriber.stop())
        .isInstanceOf(RuntimeException.class);

    verify(mqttClient).close();
  }

  @Test
  @DisplayName("subscribe_messageHandlerThrowsException_exceptionPropagatedThroughListener")
  void subscribe_messageHandlerThrowsException_exceptionPropagatedThroughListener() throws MqttException {
    when(config.getTopic()).thenReturn("sensors/telemetry");
    when(clientFactory.createSubscriberClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);
    doThrow(new RuntimeException("Handler failed")).when(messageHandler).handle(anyString(), anyString());

    subscriber.start();
    subscriber.subscribe("sensors/telemetry");

    verify(mqttClient, times(2)).subscribe(anyString(), eq(1), listenerCaptor.capture());
    IMqttMessageListener listener = listenerCaptor.getAllValues().get(1);

    assertThatThrownBy(() -> listener.messageArrived("sensors/telemetry", new MqttMessage("data".getBytes())))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Handler failed");
  }
}