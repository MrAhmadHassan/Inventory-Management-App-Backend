package com.ecommerce.inventory.UserEnum;

public enum UserRoles {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    UserRoles(String authority) {
        this.authority = authority;
    }

    /** String value used by Spring Security & JWT */
    public String authority() {
        return authority;
    }

    /** Raw role name without ROLE_ prefix */
    public String roleName() {
        return name();
    }
}

