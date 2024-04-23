package com.code.auth.controller;

import java.util.List;

import com.code.auth.entity.Cart;
import com.code.auth.entity.CartItem;
import com.code.auth.entity.UserInfo;
import com.code.auth.service.CartService;
import com.code.auth.service.UserInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/retailer/carts")
public class CartController {

    private final CartService cartService;
    private final UserInfoService userService; // Add UserService

    public CartController(CartService cartService, UserInfoService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }
    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long cartId) {
        List<CartItem> cartItems = cartService.findCartItemsByCartId(cartId);
        return ResponseEntity.ok(cartItems);
    }

//    @PostMapping("/{userId}")
//    public ResponseEntity<?> createCart(@PathVariable Long userId, @RequestBody Cart cart) {
//        // Fetch the user using UserService
//        UserInfo user = userService.getUserById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//
//        // Set the user to the cart
//        cart.setUser(user);
//
//        // Save the cart with the user associated
//        Cart savedCart = cartService.saveCart(cart);
//        return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
//    }

//    @GetMapping
//    public List<Cart> getAllCarts() {
//        return cartService.findAllCarts();
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
//        return cartService.findCartById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }



    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable Long id, @RequestBody Cart cartDetails) {
        Cart updatedCart = cartService.updateCart(id, cartDetails);
        return ResponseEntity.ok(updatedCart);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.ok().build();
    }
}