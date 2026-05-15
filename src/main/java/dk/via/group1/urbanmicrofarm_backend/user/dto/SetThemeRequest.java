package dk.via.group1.urbanmicrofarm_backend.user.dto;

import dk.via.group1.urbanmicrofarm_backend.user.model.Theme;
import jakarta.validation.constraints.NotNull;

public record SetThemeRequest(
    @NotNull(message = "Theme is required") Theme theme
) {}
