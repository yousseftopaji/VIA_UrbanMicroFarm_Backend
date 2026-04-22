package dk.via.group1.urbanmicrofarm_backend.mqtt.integration;

import dk.via.group1.urbanmicrofarm_backend.mapper.mqttMapper.MqttSensorReadingMapper;
import dk.via.group1.urbanmicrofarm_backend.mqtt.client.MqttClientFactory;
import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler.MqttMessageHandler;
import dk.via.group1.urbanmicrofarm_backend.mqtt.messageHandler.SensorReadingMqttMessageHandler;
import dk.via.group1.urbanmicrofarm_backend.mqtt.publisher.DefaultMqttPublisher;
import dk.via.group1.urbanmicrofarm_backend.mqtt.publisher.MqttPublisher;
import dk.via.group1.urbanmicrofarm_backend.mqtt.subscriber.DefaultMqttSubscriber;
import dk.via.group1.urbanmicrofarm_backend.mqtt.subscriber.MqttSubscriber;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MqttCommunicationFlowIntegrationTest.TestConfig.class)
class MqttCommunicationFlowIntegrationTest {

  @Configuration
  static class TestConfig {
    @Bean
    MqttClientConfig mqttClientConfig() {
      MqttClientConfig config = new MqttClientConfig();
      config.setBroker("tcp://integration-broker:1883");
      config.setClientId("integration-client");
      config.setTopic("farm/integration/topic");
      return config;
    }

    @Bean
    MqttClientFactory mqttClientFactory() {
      return Mockito.mock(MqttClientFactory.class);
    }

    @Bean
    MqttSensorReadingMapper mqttSensorReadingMapper() {
      return Mockito.mock(MqttSensorReadingMapper.class);
    }

    @Bean
    MqttMessageHandler mqttMessageHandler(MqttSensorReadingMapper mapper) {
      return new SensorReadingMqttMessageHandler(mapper);
    }

    @Bean
    MqttSubscriber mqttSubscriber(MqttClientConfig config, MqttClientFactory factory, MqttMessageHandler handler) {
      return new DefaultMqttSubscriber(config, factory, handler);
    }

    @Bean
    MqttPublisher mqttPublisher(MqttClientFactory factory) {
      return new DefaultMqttPublisher(factory);
    }
  }

  @Autowired
  private MqttClientFactory clientFactory;

  @Autowired
  private MqttSensorReadingMapper mapper;

  @Autowired
  private MqttSubscriber subscriber;

  @Autowired
  private MqttPublisher publisher;

  private MqttClient mqttClient;

  @BeforeEach
  void setUp() throws Exception {
    reset(clientFactory, mapper);

    mqttClient = Mockito.mock(MqttClient.class);
    when(clientFactory.createClient()).thenReturn(mqttClient);
    when(mqttClient.isConnected()).thenReturn(true);
  }

  @Test
  void publishAndSubscribe_integrationFlow_deliversPayloadToMessageHandlerMapper() throws Exception {
    AtomicReference<IMqttMessageListener> listenerRef = new AtomicReference<>();

    doAnswer(invocation -> {
      listenerRef.set(invocation.getArgument(1));
      return null;
    }).when(mqttClient).subscribe(eq("farm/integration/topic"), any(IMqttMessageListener.class));

    doAnswer(invocation -> {
      IMqttMessageListener listener = listenerRef.get();
      if (listener != null) {
        listener.messageArrived(invocation.getArgument(0), invocation.getArgument(1));
      }
      return null;
    }).when(mqttClient).publish(anyString(), any(MqttMessage.class));

    subscriber.start();
    publisher.publish("farm/integration/topic", "{\"value\":44}");

    verify(mapper).fromPayload("{\"value\":44}");

    subscriber.stop();
    verify(mqttClient, atLeast(1)).disconnect();
    verify(mqttClient, atLeastOnce()).close();
  }
}

