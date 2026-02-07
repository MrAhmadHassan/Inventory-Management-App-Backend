package com.ecommerce.inventory.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceAlreadyExists extends Exception {
    public ResourceAlreadyExists(String usernameAlreadyExists) {
        super(usernameAlreadyExists);
        log.error(usernameAlreadyExists);
    }
}
