package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.UserEnum.UserStatus;
import com.ecommerce.inventory.config.JwtProperties;
import com.ecommerce.inventory.dto.LoginRequest;
import com.ecommerce.inventory.dto.RegisterRequest;
import com.ecommerce.inventory.dto.TokenResponse;
import com.ecommerce.inventory.entity.UserEntity;
import com.ecommerce.inventory.repository.UserRepository;
import com.ecommerce.inventory.service.CustomUserDetailsService;
import com.ecommerce.inventory.service.JwtService;

import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService, JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.jwtProperties = jwtProperties;
    }

    // ---------------- REGISTER ----------------
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalStateException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()
            )
        );

        var userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        tokenResponse.setTokenType("Bearer");
        tokenResponse.setExpiresIn(jwtProperties.accessTokenExpiration().toSeconds());
        return tokenResponse;

    }
}
