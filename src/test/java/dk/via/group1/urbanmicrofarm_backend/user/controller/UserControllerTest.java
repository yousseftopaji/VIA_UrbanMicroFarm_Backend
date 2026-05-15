package dk.via.group1.urbanmicrofarm_backend.user.controller;

import dk.via.group1.urbanmicrofarm_backend.security.JwtService;
import dk.via.group1.urbanmicrofarm_backend.security.SecurityConfig;
import dk.via.group1.urbanmicrofarm_backend.user.dto.*;
import dk.via.group1.urbanmicrofarm_backend.user.exception.*;
import dk.via.group1.urbanmicrofarm_backend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @WebMvcTest auto-detects JwtAuthenticationFilter (@Component Filter) and GlobalExceptionHandler
 * (@RestControllerAdvice) — only SecurityConfig needs an explicit import because it is a
 * @Configuration class that @WebMvcTest does not auto-apply.
 * JwtService is mocked so no real JWT secret is required at test time.
 */
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean UserService userService;
    @MockitoBean JwtService jwtService;

    private static final String TOKEN = "test.jwt.token";
    private static final String BEARER = "Bearer " + TOKEN;

    @BeforeEach
    void mockJwt() {
        when(jwtService.isTokenValid(TOKEN)).thenReturn(true);
        when(jwtService.extractUserId(TOKEN)).thenReturn("1");
    }

    // ── POST /api/users ───────────────────────────────────────

    @Test
    void register_validBody_returns201WithMessage() throws Exception {
        when(userService.register(any())).thenReturn(new MessageResponse("User registered successfully"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Alice\",\"email\":\"alice@example.com\",\"password\":\"password123\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void register_invalidEmail_returns422WithErrorShape() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Alice\",\"email\":\"not-an-email\",\"password\":\"password123\"}"))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.error.timestamp").exists());
    }

    @Test
    void register_missingName_returns422() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"alice@example.com\",\"password\":\"password123\"}"))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    void register_passwordTooShort_returns422() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Alice\",\"email\":\"a@b.com\",\"password\":\"short\"}"))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void register_duplicateEmail_returns409() throws Exception {
        when(userService.register(any())).thenThrow(new EmailAlreadyExistsException("Email already in use"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Alice\",\"email\":\"alice@example.com\",\"password\":\"password123\"}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error.code").value("CONFLICT"));
    }

    // ── POST /api/users/login ─────────────────────────────────

    @Test
    void login_validCredentials_returns200WithTokenAndUser() throws Exception {
        UserDto userDto = new UserDto(1L, null, "alice@example.com", "system");
        when(userService.login(any())).thenReturn(new LoginResponse("jwt.token", userDto));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"alice@example.com\",\"password\":\"password123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt.token"))
            .andExpect(jsonPath("$.user.id").value(1))
            .andExpect(jsonPath("$.user.email").value("alice@example.com"))
            .andExpect(jsonPath("$.user.theme").value("system"))
            .andExpect(jsonPath("$.user.name").doesNotExist()); // NON_NULL omits it
    }

    @Test
    void login_wrongCredentials_returns401() throws Exception {
        when(userService.login(any())).thenThrow(new InvalidCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"alice@example.com\",\"password\":\"wrong\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

    // ── DELETE /api/users/{userId} ────────────────────────────

    @Test
    void deleteUser_authenticated_returns200() throws Exception {
        when(userService.deleteUser(1L, 1L)).thenReturn(new MessageResponse("Account deleted successfully"));

        mockMvc.perform(delete("/api/users/1").header("Authorization", BEARER))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Account deleted successfully"));
    }

    @Test
    void deleteUser_noToken_returns401() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUser_differentUser_returns403() throws Exception {
        when(userService.deleteUser(2L, 1L))
            .thenThrow(new UnauthorizedOperationException("Cannot delete another user's account"));

        mockMvc.perform(delete("/api/users/2").header("Authorization", BEARER))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error.code").value("FORBIDDEN"));
    }

    @Test
    void deleteUser_userNotFound_returns404() throws Exception {
        when(userService.deleteUser(1L, 1L)).thenThrow(new UserNotFoundException("User not found: 1"));

        mockMvc.perform(delete("/api/users/1").header("Authorization", BEARER))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }

    // ── PATCH /api/users/{userId} ─────────────────────────────

    @Test
    void updateName_authenticated_returns200WithUser() throws Exception {
        UserDto dto = new UserDto(1L, "New Name", "alice@example.com", null);
        when(userService.updateName(eq(1L), eq(1L), any())).thenReturn(dto);

        mockMvc.perform(patch("/api/users/1")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Name\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id").value(1))
            .andExpect(jsonPath("$.user.name").value("New Name"))
            .andExpect(jsonPath("$.user.email").value("alice@example.com"))
            .andExpect(jsonPath("$.user.theme").doesNotExist());
    }

    @Test
    void updateName_noToken_returns401() throws Exception {
        mockMvc.perform(patch("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Name\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void updateName_blankName_returns422() throws Exception {
        mockMvc.perform(patch("/api/users/1")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\"}"))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    // ── PUT /api/users/{userId}/password ──────────────────────

    @Test
    void changePassword_authenticated_returns200() throws Exception {
        when(userService.changePassword(eq(1L), eq(1L), any()))
            .thenReturn(new MessageResponse("Password changed successfully"));

        mockMvc.perform(put("/api/users/1/password")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currentPassword\":\"oldPass\",\"newPassword\":\"newPass123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Password changed successfully"));
    }

    @Test
    void changePassword_wrongCurrent_returns401() throws Exception {
        when(userService.changePassword(eq(1L), eq(1L), any()))
            .thenThrow(new InvalidCredentialsException("Current password is incorrect"));

        mockMvc.perform(put("/api/users/1/password")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currentPassword\":\"wrong\",\"newPassword\":\"newPass123\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

    @Test
    void changePassword_noToken_returns401() throws Exception {
        mockMvc.perform(put("/api/users/1/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currentPassword\":\"x\",\"newPassword\":\"newPass123\"}"))
            .andExpect(status().isUnauthorized());
    }

    // ── PUT /api/users/{userId}/email ─────────────────────────

    @Test
    void changeEmail_authenticated_returns200WithUser() throws Exception {
        UserDto dto = new UserDto(1L, null, "new@example.com", null);
        when(userService.changeEmail(eq(1L), eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/users/1/email")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currentPassword\":\"pass\",\"newEmail\":\"new@example.com\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id").value(1))
            .andExpect(jsonPath("$.user.email").value("new@example.com"))
            .andExpect(jsonPath("$.user.name").doesNotExist())
            .andExpect(jsonPath("$.user.theme").doesNotExist());
    }

    @Test
    void changeEmail_wrongPassword_returns401() throws Exception {
        when(userService.changeEmail(eq(1L), eq(1L), any()))
            .thenThrow(new InvalidCredentialsException("Current password is incorrect"));

        mockMvc.perform(put("/api/users/1/email")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currentPassword\":\"wrong\",\"newEmail\":\"new@example.com\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

    @Test
    void changeEmail_takenEmail_returns409() throws Exception {
        when(userService.changeEmail(eq(1L), eq(1L), any()))
            .thenThrow(new EmailAlreadyExistsException("Email already in use"));

        mockMvc.perform(put("/api/users/1/email")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currentPassword\":\"pass\",\"newEmail\":\"taken@example.com\"}"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error.code").value("CONFLICT"));
    }

    // ── PATCH /api/users/{userId}/theme ───────────────────────

    @Test
    void setTheme_authenticated_returns200WithUser() throws Exception {
        UserDto dto = new UserDto(1L, null, "alice@example.com", "dark");
        when(userService.setTheme(eq(1L), eq(1L), any())).thenReturn(dto);

        mockMvc.perform(patch("/api/users/1/theme")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"theme\":\"DARK\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.id").value(1))
            .andExpect(jsonPath("$.user.email").value("alice@example.com"))
            .andExpect(jsonPath("$.user.theme").value("dark"))
            .andExpect(jsonPath("$.user.name").doesNotExist());
    }

    @Test
    void setTheme_lowercase_returns200() throws Exception {
        UserDto dto = new UserDto(1L, null, "alice@example.com", "light");
        when(userService.setTheme(eq(1L), eq(1L), any())).thenReturn(dto);

        mockMvc.perform(patch("/api/users/1/theme")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"theme\":\"light\"}")) // lowercase accepted via @JsonCreator
            .andExpect(status().isOk());
    }

    @Test
    void setTheme_invalidTheme_returns400() throws Exception {
        mockMvc.perform(patch("/api/users/1/theme")
                .header("Authorization", BEARER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"theme\":\"rainbow\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

    @Test
    void setTheme_noToken_returns401() throws Exception {
        mockMvc.perform(patch("/api/users/1/theme")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"theme\":\"DARK\"}"))
            .andExpect(status().isUnauthorized());
    }
}
