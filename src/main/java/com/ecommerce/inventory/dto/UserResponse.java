package com.ecommerce.inventory.dto;

import com.ecommerce.inventory.UserEnum.UserStatus;
import com.ecommerce.inventory.entity.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String username;
    private String email;
    private UserStatus status;
    private Set<RoleEntity> roles = new HashSet<>();
    private String accessToken;
}
