package com.ecommerce.inventory.service;

import com.ecommerce.inventory.UserEnum.UserRoles;
import com.ecommerce.inventory.UserEnum.UserStatus;
import com.ecommerce.inventory.config.JwtProperties;
import com.ecommerce.inventory.domain.CustomUserDetails;
import com.ecommerce.inventory.dto.LoginRequest;
import com.ecommerce.inventory.dto.RegisterRequest;
import com.ecommerce.inventory.dto.TokenResponse;
import com.ecommerce.inventory.dto.UserResponse;
import com.ecommerce.inventory.entity.RoleEntity;
import com.ecommerce.inventory.entity.UserEntity;
import com.ecommerce.inventory.exceptions.ResourceAlreadyExists;
import com.ecommerce.inventory.repository.RoleRepository;
import com.ecommerce.inventory.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtService jwtService, JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    public UserResponse registerUser(RegisterRequest request) throws ResourceAlreadyExists {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResourceAlreadyExists("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already exists");
        }

        RoleEntity userRole = roleRepository.findByName(UserRoles.USER.roleName())
                .orElseThrow(() -> new IllegalStateException("USER role not found"));


        UserEntity user = new UserEntity();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus(UserStatus.ACTIVE);
        user.getRoles().add(userRole);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateAccessToken(customUserDetails);
        user.setToken(accessToken);
        UserEntity userSaved = userRepository.save(user);


        return mapToResponse(userSaved);
    }

    private UserResponse mapToResponse(UserEntity userSaved) {

        UserResponse userResponse = new UserResponse();

        userResponse.setId(userSaved.getId());
        userResponse.setUsername(userSaved.getUsername());
        userResponse.setEmail(userSaved.getEmail());
        userResponse.setStatus(userSaved.getStatus());
        userResponse.setRoles(userSaved.getRoles());
        userResponse.setAccessToken(userSaved.getToken());
         return userResponse;
    }

//    private UserResponse mapCustomUserDetailToResponse(CustomUserDetails userSaved) {
//
//        UserResponse userResponse = new UserResponse();
//
//        userResponse.setId(userSaved.getId());
//        userResponse.setUsername(userSaved.getUsername());
////        userResponse.setAccessToken(userSaved.);
//        return userResponse;
//    }

    @Scheduled(fixedRate = 10000)
    public void getAllUsersThroughSchedulerFixRate(){
        List<UserEntity> usersDB = userRepository.findAll();
        log.info("Logging users detail");
        log.info(String.valueOf(usersDB.size()));
        log.info(usersDB.toString());
    }

    public TokenResponse authenticate(LoginRequest request) {
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
