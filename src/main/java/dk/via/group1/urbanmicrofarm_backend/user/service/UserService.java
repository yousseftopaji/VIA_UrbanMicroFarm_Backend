package dk.via.group1.urbanmicrofarm_backend.user.service;

import dk.via.group1.urbanmicrofarm_backend.security.JwtService;
import dk.via.group1.urbanmicrofarm_backend.user.dto.*;
import dk.via.group1.urbanmicrofarm_backend.user.exception.*;
import dk.via.group1.urbanmicrofarm_backend.user.mapper.UserMapper;
import dk.via.group1.urbanmicrofarm_backend.user.model.User;
import dk.via.group1.urbanmicrofarm_backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already in use: " + request.email());
        }
        userRepository.save(userMapper.toEntity(request));
        log.info("User registered: email={}", request.email());
        return new MessageResponse("User registered successfully");
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> {
                log.warn("Login failed - email not found: {}", request.email());
                return new InvalidCredentialsException("Invalid email or password");
            });

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            log.warn("Login failed - wrong password: userId={}", user.getId());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId());
        log.info("Login successful: userId={}", user.getId());
        return new LoginResponse(token, userMapper.toLoginDto(user));
    }

    public MessageResponse deleteUser(Long userId, Long authenticatedUserId) {
        checkOwnership(userId, authenticatedUserId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        userRepository.delete(user);
        log.info("Account deleted: userId={}", userId);
        return new MessageResponse("Account deleted successfully");
    }

    public UserDto updateName(Long userId, Long authenticatedUserId, UpdateNameRequest request) {
        checkOwnership(userId, authenticatedUserId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        user.setName(request.name());
        return userMapper.toNameDto(userRepository.save(user));
    }

    public MessageResponse changePassword(Long userId, Long authenticatedUserId, ChangePasswordRequest request) {
        checkOwnership(userId, authenticatedUserId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            log.warn("Password change failed - wrong current password: userId={}", userId);
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        log.info("Password changed: userId={}", userId);
        return new MessageResponse("Password changed successfully");
    }

    public UserDto changeEmail(Long userId, Long authenticatedUserId, ChangeEmailRequest request) {
        checkOwnership(userId, authenticatedUserId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            log.warn("Email change failed - wrong current password: userId={}", userId);
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        if (userRepository.existsByEmail(request.newEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + request.newEmail());
        }

        user.setEmail(request.newEmail());
        log.info("Email changed: userId={}", userId);
        return userMapper.toEmailDto(userRepository.save(user));
    }

    public UserDto setTheme(Long userId, Long authenticatedUserId, SetThemeRequest request) {
        checkOwnership(userId, authenticatedUserId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        user.setTheme(request.theme());
        return userMapper.toThemeDto(userRepository.save(user));
    }

    private void checkOwnership(Long userId, Long authenticatedUserId) {
        if (!userId.equals(authenticatedUserId)) {
            throw new UnauthorizedOperationException(
                "Cannot perform operation on another user's account");
        }
    }
}
