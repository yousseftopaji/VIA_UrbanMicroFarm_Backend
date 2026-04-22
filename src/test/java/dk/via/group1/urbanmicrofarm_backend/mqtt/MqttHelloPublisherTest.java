package dk.via.group1.urbanmicrofarm_backend.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class MqttHelloPublisherTest {

  @Test
  void run_oneExecution_publishesHelloMessageAndClosesClient() throws Exception {
    MqttHelloPublisher publisher = new MqttHelloPublisher();
    ReflectionTestUtils.setField(publisher, "broker", "tcp://localhost:1883");
    ReflectionTestUtils.setField(publisher, "clientId", "zombies-hello-client");
    ReflectionTestUtils.setField(publisher, "topic", "farm/test/hello");

    try (MockedConstruction<MqttClient> mockedConstruction = Mockito.mockConstruction(MqttClient.class)) {
      publisher.run(new DefaultApplicationArguments(new String[0]));

      MqttClient client = mockedConstruction.constructed().getFirst();

      ArgumentCaptor<MqttConnectOptions> optionsCaptor = ArgumentCaptor.forClass(MqttConnectOptions.class);
      verify(client).connect(optionsCaptor.capture());
      assertNotNull(optionsCaptor.getValue());
      assertEquals(10, optionsCaptor.getValue().getConnectionTimeout());

      ArgumentCaptor<MqttMessage> messageCaptor = ArgumentCaptor.forClass(MqttMessage.class);
      verify(client).publish(eq("farm/test/hello"), messageCaptor.capture());
      assertEquals("{\"message\":\"hello from backend\"}",
          new String(messageCaptor.getValue().getPayload(), StandardCharsets.UTF_8));
      assertEquals(1, messageCaptor.getValue().getQos());

      verify(client).disconnect();
      verify(client).close();
    }
  }
}

