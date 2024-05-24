package com.code.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Product> findAllByCategoryId(Long id){
        List<Product> result = new ArrayList<>();
        if(categoryRepository.findById(id).isPresent()){
            Category category = categoryRepository.findById(id).get();
            result = productRepository.findAllByCategory(category);
        }
        return result;
    }

    public List<Product> findAllProductsByUser(Long userId) {
        return productRepository.findAllByUserId(userId);
    }

    public Optional<Product> findProductByIdAndUser(Long userId, Long productId) {
        return productRepository.findByIdAndUserId(productId, userId);
    }

    @Transactional
    public Product updateProduct(Long userId, Long productId, ProductDto productDetails) {
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
        Optional<Product> product = findProductByIdAndUser(userId, productId);
        product.ifPresent(prod -> productRepository.deleteById(prod.getId()));
    }

    public List<ProductDto> searchProductsByName(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name)
                .stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getBrand(),
                        product.getCategory().getCategoryId(),
                        product.getStockQuantity(),
                        product.getAvailableSizes(),
                        product.getAvailableColors()))
                .collect(Collectors.toList());
    }
}