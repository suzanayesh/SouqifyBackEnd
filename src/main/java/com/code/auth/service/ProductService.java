package com.code.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.code.auth.dto.user.ProductDto;
import com.code.auth.entity.Category;
import com.code.auth.entity.Product;
import com.code.auth.entity.ProductPic;
import com.code.auth.entity.UserInfo;
import com.code.auth.repo.CategoryRepository;
import com.code.auth.repo.ProductPicRepositpry;
import com.code.auth.repo.ProductRepository;
import com.code.auth.repo.UserInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserInfoRepository userInfoRepository;
    private final CategoryRepository categoryRepository;

    private final ProductPicRepositpry productPicRepositpry;

    public ProductService(ProductRepository productRepository, UserInfoRepository userInfoRepository, CategoryRepository categoryRepository, ProductPicRepositpry productPicRepositpry) {
        this.productRepository = productRepository;
        this.userInfoRepository = userInfoRepository;
        this.categoryRepository = categoryRepository;
        this.productPicRepositpry = productPicRepositpry;
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
        product.setModelNumber(productDto.getModelNumber());

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

    public List<ProductDto> findAllByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoryId(categoryId);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
        product.setModelNumber(productDetails.getModelNumber());

        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setBrand(productDetails.getBrand());
        product.setAvailableColors(productDetails.getAvailableColors());
        product.setAvailableSizes(productDetails.getAvailableSizes());

        return productRepository.save(product);
    }

    private ProductDto convertToDTO(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setModelNumber(product.getModelNumber());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setBrand(product.getBrand());
        dto.setAvailableSizes(product.getAvailableSizes());
        dto.setAvailableColors(product.getAvailableColors());
        dto.setUserName(product.getUser().getName());  // Assuming `UserInfo` has a `getName` method
        if(productPicRepositpry.findByProductId(product.getId()).isPresent()){
            ProductPic productPic = productPicRepositpry.findByProductId(product.getId()).get();
            dto.setUrl(productPic.getUrl());
        }else{
            dto.setUrl("../../../assets/random/1.webp");
        }
        return dto;
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
                        product.getModelNumber(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getBrand(),
                        product.getCategory().getCategoryId(),
                        product.getStockQuantity(),
                        product.getAvailableSizes(),
                        product.getAvailableColors(),
                        product.getProductName(),
                        productPicRepositpry.findByProductId(product.getId()).get().getUrl()
                ))
                .collect(Collectors.toList());
    }
}