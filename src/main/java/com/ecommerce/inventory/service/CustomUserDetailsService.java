package com.ecommerce.inventory.service;

import com.ecommerce.inventory.domain.CustomUserDetails;
import com.ecommerce.inventory.entity.UserEntity;
import com.ecommerce.inventory.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
