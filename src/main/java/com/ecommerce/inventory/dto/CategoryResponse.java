package com.ecommerce.inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CategoryResponse {

    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp lastUpdatedAt;
}
