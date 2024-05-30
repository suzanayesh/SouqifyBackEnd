package com.code.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.code.auth.config.UserInfoDetails;
import com.code.auth.dto.user.ProductDto;
import com.code.auth.entity.Product;
import com.code.auth.entity.UserInfo;
import com.code.auth.service.ProductService;
import com.code.auth.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private UserInfoService userService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProductsByUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserIdFromUserDetails(userDetails);
        return productService.findAllProductsByUser(userId);
    }

    @PostMapping("supplier/addProduct")
    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
    public ResponseEntity<Map<String, Object>> addProductToUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProductDto productDto) {
        Long userId = getUserIdFromUserDetails(userDetails);
        Product savedProduct = productService.addProductToUser(userId, productDto);

        Map<String, Object> response = new HashMap<>();
        response.put("productName", savedProduct.getProductName());
        response.put("description", savedProduct.getDescription());
        response.put("price", savedProduct.getPrice());
        response.put("stockQuantity", savedProduct.getStockQuantity());
        response.put("categoryId", savedProduct.getCategory().getCategoryId());
        response.put("availableSizes", savedProduct.getAvailableSizes());
        response.put("availableColors", savedProduct.getAvailableColors());
        response.put("brand", savedProduct.getBrand());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "get-all-by-category/{categoryId}")
    public ResponseEntity<?> getAllByCategory(@PathVariable Long categoryId){
        List<Product> result = productService.findAllByCategoryId(categoryId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long productId) {
        Long userId = getUserIdFromUserDetails(userDetails);
        try {
            productService.deleteProductByUserAndId(userId, productId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product successfully deleted.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search/products/{name}")
    public ResponseEntity<List<ProductDto>> searchProducts(@PathVariable String name) {
        List<ProductDto> products = productService.searchProductsByName(name);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserInfo) {
            return (long) ((UserInfo) userDetails).getId();
        } else if (userDetails instanceof UserInfoDetails) {
            return (long) ((UserInfoDetails) userDetails).getUserInfo().getId();
        } else {
            throw new UsernameNotFoundException("User details implementation not recognized");
        }
    }
}