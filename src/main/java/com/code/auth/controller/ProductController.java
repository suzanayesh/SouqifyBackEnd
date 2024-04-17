package com.code.auth.controller;


import java.util.List;

import com.code.auth.dto.user.ProductDto;
import com.code.auth.entity.Product;
import com.code.auth.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users/{userId}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProductsByUser(@PathVariable Long userId) {
        return productService.findAllProductsByUser(userId);
    }

    // This method adds a product based on ProductDto to a specific user
    @PostMapping("supplier/addProduct")
    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
    public ResponseEntity<Product> addProductToUser(@PathVariable Long userId, @RequestBody ProductDto productDto) {
        Product savedProduct = productService.addProductToUser(userId, productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductByIdAndUser(@PathVariable Long userId, @PathVariable Long id) {
        return productService.findProductByIdAndUser(userId, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Removed the createProduct method to avoid the mapping conflict
    // If needed, its functionality can be merged into the addProductToUser method

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long userId, @PathVariable Long id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(userId, id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long userId, @PathVariable Long id) {
        productService.deleteProductByUserAndId(userId, id);
        return ResponseEntity.ok().build();
    }
}
