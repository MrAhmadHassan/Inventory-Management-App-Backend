package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.ProductRequest;
import com.ecommerce.inventory.dto.ProductResponse;
import com.ecommerce.inventory.entity.Category;
import com.ecommerce.inventory.entity.Product;
import com.ecommerce.inventory.exceptions.ResourceNotFoundException;
import com.ecommerce.inventory.repository.CategoryRepository;
import com.ecommerce.inventory.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;


    public List<ProductResponse> getAllProducts(){
        List<Product> productsFromDB = productRepository.findAll();
        return mapToResponseList(productsFromDB);
    }

    public ProductResponse mapToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setCategory(product.getCategory().getName());
        productResponse.setSku(product.getSku());
        productResponse.setPrice(product.getPrice());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setCategoryId(product.getCategory().getId());
        productResponse.setStatus(product.getStatus());
        return productResponse;
    }

    public List<ProductResponse> mapToResponseList(List<Product> products) {
        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }


    public ProductResponse createProduct(ProductRequest request) throws ResourceNotFoundException {

        // 🔒 Validate category existence
        Category category = categoryRepository.findByName(request.getCategory());

        if (category == null){
            category = new Category();
            category.setName(request.getCategory());
            category.setDescription(request.getCategory()+ " new Category added by Admin");
            categoryRepository.save(category);
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());
        product.setCategory(category);


        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }


    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) throws ResourceNotFoundException {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // update fields
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());
        product.setSku(request.getSku());
        product.setQuantity(request.getQuantity());

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }
}
