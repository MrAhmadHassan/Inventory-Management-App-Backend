package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.ProductRequest;
import com.ecommerce.inventory.dto.ProductResponse;
import com.ecommerce.inventory.exceptions.ResourceNotFoundException;
import com.ecommerce.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<ProductResponse> productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(productService.getById(id));
//    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestBody ProductRequest request) {
        ProductResponse productResponse = null;
        try {
            productResponse = productService.createProduct(request);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(productResponse,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

