//package com.code.auth.service;
//
//
//import java.util.List;
//import java.util.Optional;
//
//import com.code.auth.dto.user.CartItemDTO;
//import com.code.auth.entity.Cart;
//import com.code.auth.entity.CartItem;
//import com.code.auth.entity.Color;
//import com.code.auth.entity.Product;
//import com.code.auth.repo.CartItemRepository;
//import com.code.auth.repo.CartRepository;
//import com.code.auth.repo.ProductRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class CartItemService {
//
//    private final CartItemRepository cartItemRepository;
//    private final CartRepository cartRepository;
//    private final ProductRepository productRepository;
//
//    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository) {
//        this.cartItemRepository = cartItemRepository;
//        this.cartRepository = cartRepository;
//        this.productRepository = productRepository;
//    }
//
//    public List<CartItem> findAllCartItems() {
//        return cartItemRepository.findAll();
//    }
//
//    public Optional<CartItem> findCartItemById(Long id) {
//        return cartItemRepository.findById(id);
//    }
//
//    public CartItem saveCartItem(CartItem cartItem) {
//        return cartItemRepository.save(cartItem);
//    }
//
////    @Transactional
////    public CartItem updateCartItem(Long id, CartItem cartItemDetails) {
////        CartItem cartItem = cartItemRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("CartItem not found with id " + id));
////        cartItem.setQuantity(cartItemDetails.getQuantity());
////        cartItem.setPrice(cartItemDetails.getPrice());
////        cartItem.setTotal(cartItemDetails.getPrice() * cartItemDetails.getQuantity()); // Calculate total based on the current price and quantity
////        return cartItemRepository.save(cartItem);
////    }
//@Transactional
//public CartItem updateCartItem(Long id, int quantity, double price) {
//    CartItem cartItem = cartItemRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));
//
//    cartItem.setQuantity(quantity);
//    cartItem.setPrice(price);
//    cartItem.setTotal(quantity * price);  // Assuming you want to update the total price as well
//
//    return cartItemRepository.save(cartItem);
//}
//    public void deleteCartItem(Long id) {
//        CartItem cartItem = cartItemRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));
//        cartItemRepository.delete(cartItem);
//    }
////    public void deleteCartItem(Long id) {
////        cartItemRepository.deleteById(id);
////    }
//
//    @Transactional
//    public CartItem addCartItemToCart(Long cartId, Long productId, int quantity, double price,String productName, double total, Color color) {
//        if (cartId == null || productId == null) {
//            throw new IllegalArgumentException("Cart ID and Product ID must not be null");
//        }
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new RuntimeException("Cart not found with id " + cartId));
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
//
//        CartItem cartItem = new CartItem();
//        cartItem.setCart(cart);
//        cartItem.setProduct(product);
//        cartItem.setQuantity(quantity);
//        cartItem.setPrice(price);
//        cartItem.setColor(color);
//        cartItem.setProductName(productName);
//        cartItem.setTotal(price*quantity); // Calculate total
//
//        return cartItemRepository.save(cartItem);
//    }
//    public CartItemDTO convertToCartItemDTO(CartItem cartItem) {
//        CartItemDTO dto = new CartItemDTO();
//        dto.setProductId(cartItem.getProduct().getId());
//        dto.setProductName(cartItem.getProductName());
//        dto.setQuantity(cartItem.getQuantity());
//        dto.setPrice(cartItem.getPrice());
//        dto.setColor(cartItem.getColor());
//        dto.setTotal(cartItem.getTotal());
//        return dto;
//    }
//}


package com.code.auth.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.code.auth.dto.user.AllCartItemsDTO;
import com.code.auth.dto.user.CartItemDTO;
import com.code.auth.dto.user.ProductDto;
import com.code.auth.entity.*;
import com.code.auth.repo.CartItemRepository;
import com.code.auth.repo.CartRepository;
import com.code.auth.repo.ProductPicRepositpry;
import com.code.auth.repo.ProductRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.events.EndDocument;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    private final UserInfoService userInfoService;

    private final EntityManager entityManager;

    private final ProductService productService;

    private final ProductPicService productPicService;

    private final ProductPicRepositpry productPicRepositpry;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository, UserInfoService userInfoService, EntityManager entityManager, ProductService productService, ProductPicService productPicService, ProductPicRepositpry productPicRepositpry) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userInfoService = userInfoService;
        this.entityManager = entityManager;
        this.productService = productService;
        this.productPicService = productPicService;
        this.productPicRepositpry = productPicRepositpry;
    }

    public List<AllCartItemsDTO> findAllCartItems() {
        List<CartItem> items = cartItemRepository.findAll();
        return items.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CartItem> findCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    //    @Transactional
//    public CartItem updateCartItem(Long id, CartItem cartItemDetails) {
//        CartItem cartItem = cartItemRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("CartItem not found with id " + id));
//        cartItem.setQuantity(cartItemDetails.getQuantity());
//        cartItem.setPrice(cartItemDetails.getPrice());
//        cartItem.setTotal(cartItemDetails.getPrice() * cartItemDetails.getQuantity()); // Calculate total based on the current price and quantity
//        return cartItemRepository.save(cartItem);
//    }
    @Transactional
    public CartItem updateCartItem(Long id, int quantity, List<String> colors, List<String> sizes) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id: " + id));

        cartItem.setQuantity(quantity);
        cartItem.setTotal(quantity * cartItem.getProduct().getPrice());  // Assuming you want to update the total price as well
        cartItem.setColors(colors);
        cartItem.setSizes(sizes);
        return cartItemRepository.save(cartItem);
    }
    @Transactional
    public void deleteCartItem(Long id) {
        Logger logger = LoggerFactory.getLogger(getClass());

        logger.info("Attempting to delete cart item with id: {}", id);

        CartItem cartItem = entityManager.find(CartItem.class, id);

        try {
            entityManager.remove(cartItem);
            entityManager.flush();  // Force the persistence context to be synchronized with the database
            logger.info("Cart item with id: {} has been successfully deleted", id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting cart item with id: {}", id, e);
            throw e;
        }
    }
//    public void deleteCartItem(Long id) {
//        cartItemRepository.deleteById(id);
//    }


    //the new one goes here 23.05

    public CartItem addCartItemToCart(CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findByUserId(userInfoService.getCurrentUserInfo().getId());
        if(cart == null){
            cart = new Cart();
            cart.setUser(userInfoService.getCurrentUserInfo());
            cartRepository.save(cart);
        }
        Optional<Product> optionalProduct = productRepository.findById(cartItemDTO.getProductId());
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Product not found with id: " + cartItemDTO.getProductId());
        }

        Product product = optionalProduct.get();

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItem.setTotal((cartItemDTO.getQuantity() * product.getPrice()));
        cartItem.setColors(cartItemDTO.getSelectedColors());
        cartItem.setSizes(cartItemDTO.getSelectedSizes());

        cartItemRepository.save(cartItem);
        return cartItem;
    }

    private AllCartItemsDTO convertToDTO(CartItem cartItem) {
        AllCartItemsDTO dto = new AllCartItemsDTO();
        if(productPicRepositpry.findByProductId(cartItem.getProduct().getId()).isPresent()){
            dto.setUrl(productPicRepositpry.findByProductId(cartItem.getProduct().getId()).get().getUrl());
        }

        dto.setCartItemId(cartItem.getCartItemId());
        dto.setModelNumber(cartItem.getProduct().getModelNumber());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setColors(cartItem.getColors());
        dto.setSizes(cartItem.getSizes());
        dto.setTotal(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }



    // the old one
//    @Transactional
//    public CartItem addCartItemToCart(Long cartId, Long productId, int quantity, double price,String productName, double total, Color color) {
//        if (cartId == null || productId == null) {
//            throw new IllegalArgumentException("Cart ID and Product ID must not be null");
//        }
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new RuntimeException("Cart not found with id " + cartId));
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
//
//        CartItem cartItem = new CartItem();
//        cartItem.setCart(cart);
//        cartItem.setProduct(product);
//        cartItem.setQuantity(quantity);
//        cartItem.setPrice(price);
//        cartItem.setColor(color);
//        cartItem.setProductName(productName);
//        cartItem.setTotal(price*quantity); // Calculate total
//
//        return cartItemRepository.save(cartItem);
//    }
//    public CartItemDTO convertToCartItemDTO(CartItem cartItem) {
//        CartItemDTO dto = new CartItemDTO();
//        dto.setProductId(cartItem.getProduct().getId());
//        dto.setProductName(cartItem.getProductName());
//        dto.setQuantity(cartItem.getQuantity());
//        dto.setPrice(cartItem.getPrice());
//        dto.setColor(cartItem.getColor());
//        dto.setTotal(cartItem.getTotal());
//        return dto;
//    }
}