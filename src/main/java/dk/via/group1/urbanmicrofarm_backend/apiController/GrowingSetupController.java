package dk.via.group1.urbanmicrofarm_backend.apiController;

import dk.via.group1.urbanmicrofarm_backend.application.domain.GrowingSetup;
import dk.via.group1.urbanmicrofarm_backend.application.services.growing_setup_service.GrowingSetupService;
import dk.via.group1.urbanmicrofarm_backend.dto.MessageResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.growingSetup.GrowingSetupAssignDto; // Remember to rename to AssignDto if you prefer
import dk.via.group1.urbanmicrofarm_backend.dto.growingSetup.GrowingSetupResponseDto;
import dk.via.group1.urbanmicrofarm_backend.dto.growingSetup.PatchLocationDto;
import dk.via.group1.urbanmicrofarm_backend.mapper.apiMapper.GrowingSetupApiMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/growingsetups")
public class GrowingSetupController {

  private final GrowingSetupService service;
  private final GrowingSetupApiMapper apiMapper;

  public GrowingSetupController(GrowingSetupService service, GrowingSetupApiMapper apiMapper) {
    this.service = service;
    this.apiMapper = apiMapper;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public GrowingSetupResponseDto assignSetupToUser(@RequestBody GrowingSetupAssignDto request) {
    GrowingSetup assigned = service.assignSetupToUser(request.getUserId(), request.getSetupId());
    return apiMapper.toResponseDto(assigned);
  }

  @PatchMapping("/{setupId}")
  @ResponseStatus(HttpStatus.OK)
  public GrowingSetupResponseDto updateLocation(
      @PathVariable int setupId,
      @RequestBody PatchLocationDto patchLocationDto) {
    GrowingSetup updated = service.updateSetupLocation(setupId, patchLocationDto.getLocation());
    return apiMapper.toResponseDto(updated);
  }

  @DeleteMapping("/{setupId}")
  @ResponseStatus(HttpStatus.OK)
  public MessageResponseDto disconnectSetup(@PathVariable int setupId) {
    service.disconnectSetup(setupId);
    return new MessageResponseDto("Setup disconnected successfully");
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<GrowingSetupResponseDto.GrowingSetupDetails> getSetupsForUser(
      @RequestParam("userId") int userId) {

    return service.getSetupsForUser(userId)
        .stream()
        .map(setup -> apiMapper.toResponseDto(setup).getGrowingSetup())
        .collect(Collectors.toList());
  }
}