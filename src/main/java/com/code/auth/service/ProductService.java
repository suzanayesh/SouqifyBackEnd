package com.code.auth.service;


import java.util.List;
import java.util.Optional;

import com.code.auth.dto.user.ProductDto;
import com.code.auth.entity.Category;
import com.code.auth.entity.Product;
import com.code.auth.entity.UserInfo;
import com.code.auth.repo.CategoryRepository;
import com.code.auth.repo.ProductRepository;
import com.code.auth.repo.UserInfoRepository;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserInfoRepository userInfoRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, UserInfoRepository userInfoRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userInfoRepository = userInfoRepository;
        this.categoryRepository = categoryRepository;
    }


    public Product addProductToUser(Long userId, ProductDto productDto) {
        // Fetch the user by ID. If not found, throw an exception.
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Fetch the category by ID. If not found, throw an exception.
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));

        // Create a new Product entity from ProductDto
        Product product = new Product();
        product.setProductName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setAvailableSizes(productDto.getAvailableSizes()); // New line to set sizes
        product.setAvailableColors(productDto.getAvailableColors()); // New line to set colors
        product.setUser(user);
        product.setBrand(productDto.getBrand());// Set the user
        product.setCategory(category); // Set the category

        // Save the Product entity to the database
        return productRepository.save(product);
    }

    public List<Product> findAllProductsByUser(Long userId) {
        return productRepository.findAllByUserId(userId);
    }
//    public List<Product> findProductsByName(String productName) {
//        return productRepository.findByName(productName);
//    }
    public Optional<Product> findProductByIdAndUser(Long userId, Long productId) {
        // Implement fetching by user and product ID
        return productRepository.findByIdAndUserId(productId, userId);
    }

    public Product saveProduct(Long userId, Product product) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        Category category = categoryRepository.findByName(product.getCategory().getName())
                .orElseThrow(() -> new RuntimeException("Category not found with name: " + product.getCategory().getName()));

        product.setUser(user);
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product addProduct(Long userId, Long productId, Product productDetails) {
        Product product = findProductByIdAndUser(userId, productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId + " for user " + userId));

        // Update fields
        product.setProductName(productDetails.getProductName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
product.setBrand(productDetails.getBrand());

        // Assume category is handled similarly to saveProduct
        return productRepository.save(product);
    }

    public void deleteProductByUserAndId(Long userId, Long id) {
        // Implement delete operation considering the user context
        productRepository.deleteByIdAndUserId(id, userId);
    }
}
