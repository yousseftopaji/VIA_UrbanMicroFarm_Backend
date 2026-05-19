package dk.via.group1.urbanmicrofarm_backend.application.services.growing_setup_service;

import dk.via.group1.urbanmicrofarm_backend.application.domain.GrowingSetup;
import java.util.List;

public interface GrowingSetupService {

  GrowingSetup assignSetupToUser(Long userId, String serialNumber);

  GrowingSetup updateSetupLocation(String serialNumber, String location);

  void disconnectSetup(String serialNumber);

  List<GrowingSetup> getSetupsForUser(Long userId);
}