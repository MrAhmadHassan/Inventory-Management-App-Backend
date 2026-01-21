package com.ecommerce.inventory.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductResponse {
    private String name;
    private String sku;
    private String category;
    private Integer quantity;
    private BigDecimal price;
    private Long id;
    private Long categoryId;
    private String status;

}
