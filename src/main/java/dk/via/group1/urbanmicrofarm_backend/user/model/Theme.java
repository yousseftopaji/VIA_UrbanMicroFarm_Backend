package dk.via.group1.urbanmicrofarm_backend.user.model;

// Case-insensitive deserialization (light/LIGHT/Light) is handled by
// spring.jackson.mapper.accept-case-insensitive-enums=true in application.properties.
// Unknown values cause HttpMessageNotReadableException → 400 VALIDATION_ERROR.
public enum Theme {
    LIGHT, DARK, SYSTEM
}
