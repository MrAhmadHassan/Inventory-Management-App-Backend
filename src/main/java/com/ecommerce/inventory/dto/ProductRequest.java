package com.ecommerce.inventory.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductRequest {
    private String name;

    private String sku;

    private String category;   // ✅ REQUIRED

    private Integer quantity;

    private BigDecimal price;

    private String status;
}
