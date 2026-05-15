package dk.via.group1.urbanmicrofarm_backend.user.controller;

import dk.via.group1.urbanmicrofarm_backend.user.dto.*;
import dk.via.group1.urbanmicrofarm_backend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<MessageResponse> deleteUser(
            @PathVariable Long userId, Authentication authentication) {
        return ResponseEntity.ok(userService.deleteUser(userId, uid(authentication)));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> updateName(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateNameRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(Map.of("user", userService.updateName(userId, uid(authentication), request)));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<MessageResponse> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(userService.changePassword(userId, uid(authentication), request));
    }

    @PutMapping("/{userId}/email")
    public ResponseEntity<Map<String, Object>> changeEmail(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeEmailRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(Map.of("user", userService.changeEmail(userId, uid(authentication), request)));
    }

    @PatchMapping("/{userId}/theme")
    public ResponseEntity<Map<String, Object>> setTheme(
            @PathVariable Long userId,
            @Valid @RequestBody SetThemeRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(Map.of("user", userService.setTheme(userId, uid(authentication), request)));
    }

    // Extracts user ID from the JWT principal set by JwtAuthenticationFilter
    private Long uid(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}
