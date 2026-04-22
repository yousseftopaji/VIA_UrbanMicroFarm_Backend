package dk.via.group1.urbanmicrofarm_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UrbanMicroFarmBackendApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(UrbanMicroFarmBackendApplication.class, args);
  }

}
