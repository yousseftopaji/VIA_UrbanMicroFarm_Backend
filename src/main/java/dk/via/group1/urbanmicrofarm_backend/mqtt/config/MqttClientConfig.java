package dk.via.group1.urbanmicrofarm_backend.mqtt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttClientConfig
{
  private String broker;
  private String clientId;
  private String topic;

  public String getBroker()
  {
    return broker;
  }

  public void setBroker(String broker)
  {
    this.broker = broker;
  }

  public String getClientId()
  {
    return clientId;
  }

  public void setClientId(String clientId)
  {
    this.clientId = clientId;
  }

  public String getTopic()
  {
    return topic;
  }

  public void setTopic(String topic)
  {
    this.topic = topic;
  }
}
