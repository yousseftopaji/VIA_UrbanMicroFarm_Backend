package dk.via.group1.urbanmicrofarm_backend.application.services.growing_setup_service;

import dk.via.group1.urbanmicrofarm_backend.application.domain.GrowingSetup;
import java.util.List;

public interface GrowingSetupService {

  GrowingSetup assignSetupToUser(int userId, int setupId);

  GrowingSetup updateSetupLocation(int setupId, String location);

  void disconnectSetup(int setupId);

  List<GrowingSetup> getSetupsForUser(int userId);
}