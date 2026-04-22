package dk.via.group1.urbanmicrofarm_backend.mqtt.integration;

import dk.via.group1.urbanmicrofarm_backend.mqtt.config.MqttClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class MqttClientConfigBindingIntegrationTest {

  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
      .withUserConfiguration(BindingConfig.class)
      .withPropertyValues(
          "mqtt.broker=tcp://localhost:1883",
          "mqtt.client-id=binding-client",
          "mqtt.topic=farm/test/binding"
      );

  @Test
  void configurationProperties_integrationBindsBrokerClientIdAndTopic() {
    contextRunner.run(context -> {
      MqttClientConfig config = context.getBean(MqttClientConfig.class);

      assertThat(config.getBroker()).isEqualTo("tcp://localhost:1883");
      assertThat(config.getClientId()).isEqualTo("binding-client");
      assertThat(config.getTopic()).isEqualTo("farm/test/binding");
    });
  }

  @Configuration
  @EnableConfigurationProperties(MqttClientConfig.class)
  static class BindingConfig {
  }
}

