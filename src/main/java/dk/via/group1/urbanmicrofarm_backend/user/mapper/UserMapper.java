package dk.via.group1.urbanmicrofarm_backend.user.mapper;

import dk.via.group1.urbanmicrofarm_backend.user.dto.RegisterRequest;
import dk.via.group1.urbanmicrofarm_backend.user.dto.UserDto;
import dk.via.group1.urbanmicrofarm_backend.user.model.Theme;
import dk.via.group1.urbanmicrofarm_backend.user.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(RegisterRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setTheme(Theme.SYSTEM);
        return user;
    }

    // Full DTO — all four fields present
    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), lower(user.getTheme()));
    }

    // Login response: { id, email, theme } — no name
    public UserDto toLoginDto(User user) {
        return new UserDto(user.getId(), null, user.getEmail(), lower(user.getTheme()));
    }

    // PATCH name response: { id, name, email } — no theme
    public UserDto toNameDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), null);
    }

    // PUT email response: { id, email } — no name, no theme
    public UserDto toEmailDto(User user) {
        return new UserDto(user.getId(), null, user.getEmail(), null);
    }

    // PATCH theme response: { id, email, theme } — no name
    public UserDto toThemeDto(User user) {
        return new UserDto(user.getId(), null, user.getEmail(), lower(user.getTheme()));
    }

    private String lower(Theme theme) {
        return theme.name().toLowerCase();
    }
}
