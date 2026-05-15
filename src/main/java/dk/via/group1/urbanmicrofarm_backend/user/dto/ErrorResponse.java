package dk.via.group1.urbanmicrofarm_backend.user.dto;

public record ErrorResponse(ErrorDetail error) {
    public record ErrorDetail(String code, String message, String timestamp) {}
}
