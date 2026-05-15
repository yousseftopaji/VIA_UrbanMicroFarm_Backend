package dk.via.group1.urbanmicrofarm_backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequest(
    @NotBlank(message = "Current password is required") String currentPassword,
    @NotBlank(message = "New email is required") @Email(message = "Invalid email format") String newEmail
) {}
