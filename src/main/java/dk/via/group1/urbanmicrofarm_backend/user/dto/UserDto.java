package dk.via.group1.urbanmicrofarm_backend.user.dto;

// Null-field omission is configured globally via spring.jackson.default-property-inclusion=non_null
// so each mapper method can set unused fields to null and they won't appear in the JSON output.
public record UserDto(Long id, String name, String email, String theme) {}
