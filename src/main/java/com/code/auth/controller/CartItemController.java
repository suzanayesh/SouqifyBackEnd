//package com.code.auth.controller;
//
//import java.util.List;
//
//import com.code.auth.dto.user.CartItemDTO;
//import com.code.auth.entity.CartItem;
//import com.code.auth.service.CartItemService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
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
//@RequestMapping("/cart/cartItems")
//public class CartItemController {
//
//    private final CartItemService cartItemService;
//
//    public CartItemController(CartItemService cartItemService) {
//        this.cartItemService = cartItemService;
//    }
//
//    @GetMapping
//    public List<CartItem> getAllCartItems() {
//        return cartItemService.findAllCartItems();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CartItem> getCartItemById(@PathVariable Long id) {
//        return cartItemService.findCartItemById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//    @PostMapping("/add/{cartId}")
//    public ResponseEntity<CartItem> addCartItemToCart(@PathVariable Long cartId, @RequestBody CartItemDTO cartItemRequest) {
//        CartItem cartItem = cartItemService.addCartItemToCart(cartId, cartItemRequest.getProductId(), cartItemRequest.getQuantity(), cartItemRequest.getPrice(), cartItemRequest.getProductName(),cartItemRequest.getTotal(),cartItemRequest.getColor());
//        return ResponseEntity.ok(cartItem);
//    }
//
//    @PostMapping
//    public CartItem createCartItem(@RequestBody CartItem cartItem) {
//        return cartItemService.saveCartItem(cartItem);
//    }
//
////    @PutMapping("/{id}")
////    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItemDetails) {
////        CartItem updatedCartItem = cartItemService.updateCartItem(id, cartItemDetails);
////        return ResponseEntity.ok(updatedCartItem);
////    }
//@PutMapping("/{id}")
//public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem updatedCartItem) {
//    CartItem cartItem = cartItemService.updateCartItem(id, updatedCartItem.getQuantity(), updatedCartItem.getPrice());
//    return ResponseEntity.ok(cartItem);
//}
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
//        try {
//            cartItemService.deleteCartItem(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found with id: " + id);
//        }
//    }
////    @DeleteMapping("/{id}")
////    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
////        cartItemService.deleteCartItem(id);
////        return ResponseEntity.ok().build();
////    }
//}

package com.code.auth.controller;

import java.util.List;

import com.code.auth.dto.user.CartItemDTO;
import com.code.auth.entity.CartItem;
import com.code.auth.service.CartItemService;
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
@RequestMapping("/cart/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public List<CartItem> getAllCartItems() {
        return cartItemService.findAllCartItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable Long id) {
        return cartItemService.findCartItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add/{cartId}")
    public ResponseEntity<CartItem> addCartItemToCart(@PathVariable Long cartId, @RequestBody CartItemDTO cartItemRequest) {
        CartItem cartItem = cartItemService.addCartItemToCart(cartId, cartItemRequest.getProductId(), cartItemRequest.getQuantity(), cartItemRequest.getPrice(), cartItemRequest.getProductName(),cartItemRequest.getTotal(),cartItemRequest.getColor());
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping
    public CartItem createCartItem(@RequestBody CartItem cartItem) {
        return cartItemService.saveCartItem(cartItem);
    }

    //    @PutMapping("/{id}")
//    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItemDetails) {
//        CartItem updatedCartItem = cartItemService.updateCartItem(id, cartItemDetails);
//        return ResponseEntity.ok(updatedCartItem);
//    }
    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem updatedCartItem) {
        CartItem cartItem = cartItemService.updateCartItem(id, updatedCartItem.getQuantity(), updatedCartItem.getPrice());
        return ResponseEntity.ok(cartItem);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        try {
            cartItemService.deleteCartItem(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found with id: " + id);
        }
    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
//        cartItemService.deleteCartItem(id);
//        return ResponseEntity.ok().build();
//    }
}
