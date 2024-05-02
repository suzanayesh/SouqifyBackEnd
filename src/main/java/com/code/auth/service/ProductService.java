package com.code.auth.service;


import java.beans.Transient;
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
import org.springframework.transaction.annotation.Transactional;


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


//    public Product addProductToUser(Long userId, ProductDto productDto) {
//        // Fetch the user by ID. If not found, throw an exception.
//        UserInfo user = userInfoRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//
//        // Fetch the category by ID. If not found, throw an exception.
//        Category category = categoryRepository.findById(productDto.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));
//
//        Product product = new Product();
//        product.setProductName(productDto.getProductName());
//        product.setDescription(productDto.getDescription());
//        product.setPrice(productDto.getPrice());
//        product.setStockQuantity(productDto.getStockQuantity());
//        product.setAvailableSizes(productDto.getAvailableSizes()); // New line to set sizes
//        product.setAvailableColors(productDto.getAvailableColors()); // New line to set colors
//        product.setUser(user);
//        product.setBrand(productDto.getBrand());// Set the user
//        product.setCategory(category); // Set the category
//
//        // Save the Product entity to the database
//        return productRepository.save(product);
//    }
   @Transactional
    public Product addProductToUser(Long userId, ProductDto productDto) {
        UserInfo user = userInfoRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));

        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
       product.setBrand(productDto.getBrand());
       product.setStockQuantity(productDto.getStockQuantity());
        product.setAvailableSizes(productDto.getAvailableSizes());
        product.setAvailableColors(productDto.getAvailableColors());
        product.setUser(user);
        product.setCategory(category);

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

        product.setProductName(productDetails.getProductName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setBrand(productDetails.getBrand());
        product.setAvailableColors(productDetails.getAvailableColors());
        product.setAvailableSizes(productDetails.getAvailableSizes());
        return productRepository.save(product);
    }


    @Transactional
    public void deleteProductByUserAndId(Long userId, Long productId) {
        // It's clearer in this context to use 'productId' because you're also passing a 'userId'
        Optional<Product> product = findProductByIdAndUser(userId, productId);
        if (!product.isPresent()) {
            throw new RuntimeException("Product not found with id " + productId + " for user " + userId);
        }


        productRepository.deleteByIdAndUserId(productId, userId);
    }
}
