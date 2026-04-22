package dk.via.group1.urbanmicrofarm_backend.mqtt.subscriber;

import dk.via.group1.urbanmicrofarm_backend.mqtt.client.MqttClientFactory;
import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler.MqttMessageHandler;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultMqttSubscriberTest {

  @Mock
  private MqttClientConfig config;

  @Mock
  private MqttClientFactory clientFactory;

  @Mock
  private MqttMessageHandler messageHandler;

  @Mock
  private MqttClient client;

  private MqttSubscriber subscriber;

  @BeforeEach
  void setUp() {
    subscriber = new DefaultMqttSubscriber(config, clientFactory, messageHandler);
  }

  @Test
  void start_oneTopic_connectsAndSubscribesToConfiguredTopic() throws MqttException {
    when(config.getTopic()).thenReturn("farm/test/subscriber");
    when(clientFactory.createClient()).thenReturn(client);

    subscriber.start();

    verify(client).connect();
    verify(client).subscribe(eq("farm/test/subscriber"), any(IMqttMessageListener.class));
  }

  @Test
  void subscribe_zeroStartedClient_throwsIllegalStateException() {
    assertThrows(IllegalStateException.class, () -> subscriber.subscribe("farm/test/subscriber"));
  }

  @Test
  void subscribe_oneIncomingMessage_forwardsPayloadToHandler() throws Exception {
    when(config.getTopic()).thenReturn("farm/test/subscriber");
    when(clientFactory.createClient()).thenReturn(client);

    subscriber.start();

    ArgumentCaptor<IMqttMessageListener> listenerCaptor = ArgumentCaptor.forClass(IMqttMessageListener.class);
    verify(client).subscribe(eq("farm/test/subscriber"), listenerCaptor.capture());

    MqttMessage message = new MqttMessage("{\"value\":12}".getBytes(StandardCharsets.UTF_8));
    listenerCaptor.getValue().messageArrived("farm/test/subscriber", message);

    verify(messageHandler).handle("farm/test/subscriber", "{\"value\":12}");
  }

  @Test
  void stop_boundaryConnectedClient_disconnectsAndCloses() throws MqttException {
    when(config.getTopic()).thenReturn("farm/test/subscriber");
    when(clientFactory.createClient()).thenReturn(client);
    when(client.isConnected()).thenReturn(true);
    doNothing().when(client).disconnect();

    subscriber.start();
    subscriber.stop();

    verify(client).disconnect();
    verify(client).close();
  }

  @Test
  void stop_boundaryNeverStarted_doesNothing() throws MqttException {
    subscriber.stop();

    verify(client, never()).disconnect();
    verify(client, never()).close();
  }
}
