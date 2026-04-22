package dk.via.group1.urbanmicrofarm_backend.mqtt.publisher;

import dk.via.group1.urbanmicrofarm_backend.mqtt.client.MqttClientFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultMqttPublisherTest {

  @Mock
  private MqttClientFactory clientFactory;

  @Mock
  private MqttClient client;

  private MqttPublisher publisher;

  @BeforeEach
  void setUp() throws MqttException {
    when(clientFactory.createClient()).thenReturn(client);
    publisher = new DefaultMqttPublisher(clientFactory);
  }

  @Test
  void publish_onePayload_connectsPublishesDisconnectsAndCloses() throws MqttException {
    when(client.isConnected()).thenReturn(true);

    publisher.publish("farm/test/one", "hello");

    ArgumentCaptor<MqttConnectOptions> optionsCaptor = ArgumentCaptor.forClass(MqttConnectOptions.class);
    verify(client).connect(optionsCaptor.capture());

    MqttConnectOptions options = optionsCaptor.getValue();
    assertEquals(10, options.getConnectionTimeout());

    ArgumentCaptor<MqttMessage> messageCaptor = ArgumentCaptor.forClass(MqttMessage.class);
    verify(client).publish(eq("farm/test/one"), messageCaptor.capture());
    assertEquals("hello", new String(messageCaptor.getValue().getPayload(), StandardCharsets.UTF_8));
    assertEquals(1, messageCaptor.getValue().getQos());

    verify(client).disconnect();
    verify(client).close();
  }

  @Test
  void publish_zeroConnectedOnFinally_closesWithoutDisconnect() throws MqttException {
    when(client.isConnected()).thenReturn(false);

    publisher.publish("farm/test/zero", "");

    verify(client, never()).disconnect();
    verify(client).close();
  }

  @Test
  void publish_exceptionFromClient_stillTriesDisconnectAndClose() throws MqttException {
    when(client.isConnected()).thenReturn(true);
    doThrow(new MqttException(500)).when(client).publish(any(String.class), any(MqttMessage.class));

    assertThrows(MqttException.class, () -> publisher.publish("farm/test/error", "payload"));

    verify(client).disconnect();
    verify(client).close();
  }
}

