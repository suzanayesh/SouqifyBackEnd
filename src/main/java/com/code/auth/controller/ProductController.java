package com.code.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        UserInfo user = (UserInfo) userDetails;
        return productService.findAllProductsByUser((long) user.getId());
    }

    @PostMapping("supplier/addProduct")
    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
    public ResponseEntity<Map<String, Object>> addProductToUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProductDto productDto) {
        // Retrieve username or user identifier from userDetails
        String username = userDetails.getUsername();

        // Use the username to get the UserInfo from the database
        UserInfo user = (UserInfo) userService.loadUserByUsername(username);
        Product savedProduct = productService.addProductToUser((long) user.getId(), productDto);

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
        UserInfo user = (UserInfo) userDetails;
        try {
            productService.deleteProductByUserAndId((long) user.getId(), productId);
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
}

//package com.code.auth.controller;
//
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.code.auth.dto.user.ProductDto;
//import com.code.auth.entity.Product;
//import com.code.auth.entity.UserInfo;
//import com.code.auth.service.ProductService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/products")
//public class ProductController {
////    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
//
//    private final ProductService productService;
//
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//
//    @GetMapping
//    public List<Product> getAllProductsByUser(@PathVariable Long userId) {
//        return productService.findAllProductsByUser(userId);
//    }
//
//    // This method adds a product based on ProductDto to a specific user
////    @PostMapping("supplier/addProduct")
////    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
////    public ResponseEntity<Product> addProductToUser(@PathVariable Long userId, @RequestBody ProductDto productDto) {
////        Product savedProduct = productService.addProductToUser(userId, productDto);
////        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
////    }
//
//
//    @GetMapping(value = "get-all-by-category/{categoryId}")
//    public ResponseEntity<?> getAllByCategory(@PathVariable Long categoryId){
//        List<Product> result = productService.findAllByCategoryId(categoryId);
//        return ResponseEntity.ok(result);
//    }
//    // old product
////    @PostMapping("supplier/addProduct")
////    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
////    public ResponseEntity<Map<String, Object>> addProductToUser(@PathVariable Long userId, @RequestBody ProductDto productDto) {
////        Product savedProduct = productService.addProductToUser(userId, productDto);
////
////        // Create a custom response JSON object
////        Map<String, Object> response = new HashMap<>();
////        response.put("productName", savedProduct.getProductName());
////        response.put("description", savedProduct.getDescription());
////        response.put("price", savedProduct.getPrice());
////        response.put("stockQuantity", savedProduct.getStockQuantity());
////        response.put("categoryId", savedProduct.getCategory().getCategoryId());
////        response.put("availableSizes", savedProduct.getAvailableSizes());
////        response.put("availableColors", savedProduct.getAvailableColors());
////        response.put("brand", savedProduct.getBrand());
////
////        return new ResponseEntity<>(response, HttpStatus.CREATED);
////    }
//
//
//    @PostMapping("supplier/addProduct")
//    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
//    public ResponseEntity<Map<String, Object>> addProductToUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProductDto productDto) {
//        Long userId = (long) ((UserInfo) userDetails).getId(); // This assumes you have a custom User class that extends UserDetails and has a getId() method.
//        Product savedProduct = productService.addProductToUser(userId, productDto);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("productName", savedProduct.getProductName());
//        response.put("description", savedProduct.getDescription());
//        response.put("price", savedProduct.getPrice());
//        response.put("stockQuantity", savedProduct.getStockQuantity());
//        response.put("categoryId", savedProduct.getCategory().getCategoryId());
//        response.put("availableSizes", savedProduct.getAvailableSizes());
//        response.put("availableColors", savedProduct.getAvailableColors());
//        response.put("brand", savedProduct.getBrand());
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//
//
////    @GetMapping("/{id}")
////    public ResponseEntity<Product> getProductByIdAndUser(@PathVariable Long userId, @PathVariable Long id) {
////        return productService.findProductByIdAndUser(userId, id)
////                .map(ResponseEntity::ok)
////                .orElseGet(() -> ResponseEntity.notFound().build());
////    }
//
//    // Removed the createProduct method to avoid the mapping conflict
//    // If needed, its functionality can be merged into the addProductToUser method
////    @GetMapping("/search/{productName}")
////    public ResponseEntity<List<Product>> getProductsByName(@PathVariable String productName) {
////        List<Product> products = productService.findProductsByName(productName);
////        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
////    }
////    @PutMapping("/{id}")
////    public ResponseEntity<Product> addProduct(@PathVariable Long userId, @PathVariable Long id, @RequestBody Product productDetails) {
////        Product updatedProduct = productService.addProduct(userId, id, productDetails);
////        return ResponseEntity.ok(updatedProduct);
////    }
//    @DeleteMapping("/{productId}")
//    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
//    public ResponseEntity<?> deleteProduct(@PathVariable Long userId, @PathVariable Long productId) {
//        try {
//            productService.deleteProductByUserAndId(userId, productId);
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Product successfully deleted.");
//            return ResponseEntity.ok(response);
//        } catch (RuntimeException e) {
//            // Handle the exception appropriately
//            // e.g., return an error response if the product doesn't exist or the user doesn't have permission to delete it
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//    @GetMapping("/search/products/{name}")
//    public ResponseEntity<List<ProductDto>> searchProducts(@PathVariable String name) {
//        List<ProductDto> products = productService.searchProductsByName(name);
//        if (products.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(products);
//    }
//
//    // Example Debug Logging in your deleteProduct method
////    @DeleteMapping("supplier/{userId}/removeProduct/{id}")
////    @PreAuthorize("hasAuthority('SUPPLIER_PER')")
////    public ResponseEntity<?> deleteProduct(@PathVariable Long userId, @PathVariable Long id) {
//////        log.debug("Request to delete product with id {} for user {}", id, userId);
////        productService.deleteProductByUserAndId(userId, id);
////        return ResponseEntity.ok().build();
////    }
//
//}