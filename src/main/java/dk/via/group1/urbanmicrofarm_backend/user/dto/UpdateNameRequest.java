package dk.via.group1.urbanmicrofarm_backend.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameRequest(
    @NotBlank(message = "Name is required") String name
) {}
