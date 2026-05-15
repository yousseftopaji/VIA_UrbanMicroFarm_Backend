package dk.via.group1.urbanmicrofarm_backend.user.service;

import dk.via.group1.urbanmicrofarm_backend.security.JwtService;
import dk.via.group1.urbanmicrofarm_backend.user.dto.*;
import dk.via.group1.urbanmicrofarm_backend.user.exception.*;
import dk.via.group1.urbanmicrofarm_backend.user.mapper.UserMapper;
import dk.via.group1.urbanmicrofarm_backend.user.model.Theme;
import dk.via.group1.urbanmicrofarm_backend.user.model.User;
import dk.via.group1.urbanmicrofarm_backend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @Mock UserMapper userMapper;
    @InjectMocks UserService userService;

    // ── register ──────────────────────────────────────────────

    @Test
    void register_success_returnsMessage() {
        var req = new RegisterRequest("Alice", "alice@example.com", "password123");
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userMapper.toEntity(req)).thenReturn(new User());

        MessageResponse response = userService.register(req);

        assertThat(response.message()).isEqualTo("User registered successfully");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throws() {
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(
            new RegisterRequest("Alice", "alice@example.com", "password123")))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessageContaining("alice@example.com");
    }

    // ── login ─────────────────────────────────────────────────

    @Test
    void login_success_returnsTokenAndUser() {
        var req = new LoginRequest("alice@example.com", "password123");
        User user = buildUser(1L, "alice@example.com", "hash");
        var loginDto = new UserDto(1L, null, "alice@example.com", "system");

        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hash")).thenReturn(true);
        when(jwtService.generateToken(1L)).thenReturn("test.jwt.token");
        when(userMapper.toLoginDto(user)).thenReturn(loginDto);

        LoginResponse response = userService.login(req);

        assertThat(response.token()).isEqualTo("test.jwt.token");
        assertThat(response.user().id()).isEqualTo(1L);
    }

    @Test
    void login_emailNotFound_throws() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(new LoginRequest("nobody@example.com", "pass")))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_wrongPassword_throws() {
        User user = buildUser(1L, "alice@example.com", "hash");
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);

        assertThatThrownBy(() -> userService.login(new LoginRequest("alice@example.com", "wrong")))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    // ── deleteUser ────────────────────────────────────────────

    @Test
    void deleteUser_success_returnsMessage() {
        User user = buildUser(1L, "alice@example.com", "hash");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        MessageResponse response = userService.deleteUser(1L, 1L);

        assertThat(response.message()).isEqualTo("Account deleted successfully");
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(99L, 99L))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void deleteUser_crossUser_throwsForbidden() {
        assertThatThrownBy(() -> userService.deleteUser(2L, 1L))
            .isInstanceOf(UnauthorizedOperationException.class);
    }

    // ── updateName ────────────────────────────────────────────

    @Test
    void updateName_success_returnsDto() {
        User user = buildUser(1L, "alice@example.com", "hash");
        var nameDto = new UserDto(1L, "New Name", "alice@example.com", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toNameDto(user)).thenReturn(nameDto);

        UserDto result = userService.updateName(1L, 1L, new UpdateNameRequest("New Name"));

        assertThat(result.name()).isEqualTo("New Name");
        assertThat(user.getName()).isEqualTo("New Name");
    }

    @Test
    void updateName_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateName(99L, 99L, new UpdateNameRequest("X")))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateName_crossUser_throwsForbidden() {
        assertThatThrownBy(() -> userService.updateName(2L, 1L, new UpdateNameRequest("X")))
            .isInstanceOf(UnauthorizedOperationException.class);
    }

    // ── changePassword ────────────────────────────────────────

    @Test
    void changePassword_success_returnsMessage() {
        User user = buildUser(1L, "alice@example.com", "oldHash");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "oldHash")).thenReturn(true);
        when(passwordEncoder.encode("newPass123")).thenReturn("newHash");
        when(userRepository.save(user)).thenReturn(user);

        MessageResponse response = userService.changePassword(1L, 1L,
            new ChangePasswordRequest("oldPass", "newPass123"));

        assertThat(response.message()).isEqualTo("Password changed successfully");
        assertThat(user.getPasswordHash()).isEqualTo("newHash");
    }

    @Test
    void changePassword_wrongCurrent_throws() {
        User user = buildUser(1L, "alice@example.com", "hash");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword(1L, 1L,
            new ChangePasswordRequest("wrong", "newPass123")))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void changePassword_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changePassword(99L, 99L,
            new ChangePasswordRequest("x", "y12345678")))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void changePassword_crossUser_throwsForbidden() {
        assertThatThrownBy(() -> userService.changePassword(2L, 1L,
            new ChangePasswordRequest("x", "y12345678")))
            .isInstanceOf(UnauthorizedOperationException.class);
    }

    // ── changeEmail ───────────────────────────────────────────

    @Test
    void changeEmail_success_returnsDto() {
        User user = buildUser(1L, "alice@example.com", "hash");
        var emailDto = new UserDto(1L, null, "new@example.com", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "hash")).thenReturn(true);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toEmailDto(user)).thenReturn(emailDto);

        UserDto result = userService.changeEmail(1L, 1L,
            new ChangeEmailRequest("pass", "new@example.com"));

        assertThat(result.email()).isEqualTo("new@example.com");
    }

    @Test
    void changeEmail_wrongPassword_throws() {
        User user = buildUser(1L, "alice@example.com", "hash");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);

        assertThatThrownBy(() -> userService.changeEmail(1L, 1L,
            new ChangeEmailRequest("wrong", "new@example.com")))
            .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void changeEmail_newEmailTaken_throws() {
        User user = buildUser(1L, "alice@example.com", "hash");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "hash")).thenReturn(true);
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.changeEmail(1L, 1L,
            new ChangeEmailRequest("pass", "taken@example.com")))
            .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void changeEmail_crossUser_throwsForbidden() {
        assertThatThrownBy(() -> userService.changeEmail(2L, 1L,
            new ChangeEmailRequest("x", "y@y.com")))
            .isInstanceOf(UnauthorizedOperationException.class);
    }

    @Test
    void changeEmail_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changeEmail(99L, 99L,
            new ChangeEmailRequest("x", "y@y.com")))
            .isInstanceOf(UserNotFoundException.class);
    }

    // ── setTheme ──────────────────────────────────────────────

    @Test
    void setTheme_success_returnsDto() {
        User user = buildUser(1L, "alice@example.com", "hash");
        var themeDto = new UserDto(1L, null, "alice@example.com", "dark");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toThemeDto(user)).thenReturn(themeDto);

        UserDto result = userService.setTheme(1L, 1L, new SetThemeRequest(Theme.DARK));

        assertThat(result.theme()).isEqualTo("dark");
        assertThat(user.getTheme()).isEqualTo(Theme.DARK);
    }

    @Test
    void setTheme_notFound_throws() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.setTheme(99L, 99L, new SetThemeRequest(Theme.LIGHT)))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void setTheme_crossUser_throwsForbidden() {
        assertThatThrownBy(() -> userService.setTheme(2L, 1L, new SetThemeRequest(Theme.DARK)))
            .isInstanceOf(UnauthorizedOperationException.class);
    }

    private User buildUser(Long id, String email, String hash) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPasswordHash(hash);
        user.setTheme(Theme.SYSTEM);
        return user;
    }
}
