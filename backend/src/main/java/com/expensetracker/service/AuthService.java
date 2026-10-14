package com.expensetracker.service;

import com.expensetracker.dto.AuthRequest;
import com.expensetracker.dto.AuthResponse;
import com.expensetracker.model.User;
import com.expensetracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse signup(AuthRequest request) {
        String name = normalize(request.getName());
        String email = normalizeEmail(request.getEmail());
        String password = request.getPassword();

        if (name.length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));

        User saved = userRepository.save(user);
        return loginResponse(saved, createToken());
    }

    public AuthResponse login(AuthRequest request) {
        String email = normalizeEmail(request.getEmail());
        String password = request.getPassword();

        User existing = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, existing.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return loginResponse(existing, createToken());
    }

    private AuthResponse loginResponse(User user, String token) {
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), token);
    }

    private String createToken() {
        return UUID.randomUUID().toString();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeEmail(String email) {
        return normalize(email).toLowerCase();
    }
}
