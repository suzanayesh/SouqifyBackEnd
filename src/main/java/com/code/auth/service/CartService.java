package com.code.auth.service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.code.auth.entity.Cart;
import com.code.auth.entity.CartItem;
import com.code.auth.entity.Product;
import com.code.auth.repo.CartItemRepository;
import com.code.auth.repo.CartRepository;
import com.code.auth.repo.ProductRepository;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository; // Assume this exists
    private final ProductRepository productRepository;

    private final UserInfoService userInfoService;
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository  productRepository, UserInfoService userInfoService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository=productRepository;
        this.userInfoService = userInfoService;
    }
    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }

    public Optional<Cart> findCartById(Long id) {
        return cartRepository.findById(id);
    }

    public List<CartItem> findCartItemsByCartId(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));
        return cart.getCartItems();
    }

    public Cart getCartByUserId(Long id){
        return cartRepository.findByUserId(id);
    }
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCart(Long id, Cart cartDetails) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id " + id));

        // Example of updating a simple field
        // Assuming Cart has a field for tracking the last updated timestamp
        cart.setLastUpdated(cartDetails.getLastUpdated());

        // Implement logic for updating cart items
        Set<CartItem> updatedItems = new HashSet<>();
        for (CartItem itemDetails : cartDetails.getCartItems()) {
            if (itemDetails.getCartItemId() == null) {
                // If itemDetails doesn't have an ID, it's a new item
                itemDetails.setCart(cart); // Set the cart reference
                updatedItems.add(cartItemRepository.save(itemDetails));
            } else {
                // Existing item, update quantity, price, etc.
                CartItem existingItem = cartItemRepository.findById(itemDetails.getCartItemId())
                        .orElseThrow(() -> new RuntimeException("CartItem not found with id " + itemDetails.getCartItemId()));
                existingItem.setQuantity(itemDetails.getQuantity());
                // Update other fields as necessary
                existingItem.setColors(itemDetails.getColors());
                existingItem.setTotal(itemDetails.getQuantity() * itemDetails.getProduct().getPrice());
                existingItem.setSizes(itemDetails.getSizes());
                updatedItems.add(cartItemRepository.save(existingItem));
            }
        }

        // Remove items that are no longer present
        cart.getCartItems().removeIf(item -> !updatedItems.contains(item));

        // Finally, update the cart with the new set of items
// Convert Set to List before setting it on the Cart
        List<CartItem> updatedItemsList = new ArrayList<>(updatedItems);
        cart.setCartItems(updatedItemsList);
        return cartRepository.save(cart);
    }
    public void deleteCart(Long id) {
        Cart cart = cartRepository.findByUserId(userInfoService.getCurrentUserInfo().getId());
        if(cart == null){
            cart = new Cart();
            cart.setUser(userInfoService.getCurrentUserInfo());
            cartRepository.save(cart);
        }
    }
    public CartItem addCartItemToCart(Long cartId, CartItem cartItem, Long productId) {
        // Find the Cart by ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id " + cartId));

        // Find the Product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        // Associate CartItem with the found Cart and Product
        cartItem.setCart(cart);
        cartItem.setProduct(product);

        // Save the CartItem
        return cartItemRepository.save(cartItem);
    }

}