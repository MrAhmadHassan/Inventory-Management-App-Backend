package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.UserEnum.UserStatus;
import com.ecommerce.inventory.config.JwtProperties;
import com.ecommerce.inventory.dto.LoginRequest;
import com.ecommerce.inventory.dto.RegisterRequest;
import com.ecommerce.inventory.dto.TokenResponse;
import com.ecommerce.inventory.dto.UserResponse;
import com.ecommerce.inventory.entity.UserEntity;
import com.ecommerce.inventory.exceptions.ResourceAlreadyExists;
import com.ecommerce.inventory.repository.UserRepository;
import com.ecommerce.inventory.service.CustomUserDetailsService;
import com.ecommerce.inventory.service.JwtService;

import com.ecommerce.inventory.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final UserService userService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService, JwtProperties jwtProperties, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) throws ResourceAlreadyExists {

        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.OK);
    }



    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {

        TokenResponse tokenResponse = userService.authenticate(request);
        return new ResponseEntity<>(tokenResponse,HttpStatus.OK);

    }


}
