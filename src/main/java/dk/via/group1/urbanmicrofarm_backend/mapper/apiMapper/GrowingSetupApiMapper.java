package dk.via.group1.urbanmicrofarm_backend.mapper.apiMapper;

import dk.via.group1.urbanmicrofarm_backend.application.domain.GrowingSetup;
import dk.via.group1.urbanmicrofarm_backend.dto.growingSetup.GrowingSetupResponseDto;
import org.springframework.stereotype.Component;

@Component
public class GrowingSetupApiMapper {

  public GrowingSetupResponseDto toResponseDto(GrowingSetup domain) {
    if (domain == null) return null;

    GrowingSetupResponseDto.GrowingSetupDetails details = new GrowingSetupResponseDto.GrowingSetupDetails();
    details.setId(domain.getSerialNumber());
    details.setLocation(domain.getLocation());
    details.setStatus(domain.getStatus() != null && !domain.getStatus().isBlank() ? domain.getStatus() : "ACTIVE");

    return new GrowingSetupResponseDto(details);
  }
}