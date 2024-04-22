package com.code.auth.service;


import java.util.List;
import java.util.Optional;

import com.code.auth.dto.user.CartItemDTO;
import com.code.auth.entity.Cart;
import com.code.auth.entity.CartItem;
import com.code.auth.entity.Color;
import com.code.auth.entity.Product;
import com.code.auth.repo.CartItemRepository;
import com.code.auth.repo.CartRepository;
import com.code.auth.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> findAllCartItems() {
        return cartItemRepository.findAll();
    }

    public Optional<CartItem> findCartItemById(Long id) {
        return cartItemRepository.findById(id);
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public CartItem updateCartItem(Long id, CartItem cartItemDetails) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found with id " + id));
        cartItem.setQuantity(cartItemDetails.getQuantity());
        cartItem.setPrice(cartItemDetails.getPrice());
        cartItem.setTotal(cartItemDetails.getPrice() * cartItemDetails.getQuantity()); // Calculate total based on the current price and quantity
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Transactional
    public CartItem addCartItemToCart(Long cartId, Long productId, int quantity, double price,String productName, double total, Color color) {
        if (cartId == null || productId == null) {
            throw new IllegalArgumentException("Cart ID and Product ID must not be null");
        }
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id " + cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price);
        cartItem.setColor(color);
        cartItem.setProductName(productName);
        cartItem.setTotal(price*quantity); // Calculate total

        return cartItemRepository.save(cartItem);
    }
    public CartItemDTO convertToCartItemDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProductName());
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getPrice());
        dto.setColor(cartItem.getColor());
        dto.setTotal(cartItem.getTotal());
        return dto;
    }
}
