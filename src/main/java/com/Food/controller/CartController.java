package com.Food.controller;

import com.Food.models.Cart;
import com.Food.models.CartItem;
import com.Food.models.User;
import com.Food.request.AddCartItemRequest;
import com.Food.request.UpdateCardItemRequest;
import com.Food.service.CartService;
import com.Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @PutMapping("/cart/add")
    public ResponseEntity<CartItem>addItemToCart(@RequestBody AddCartItemRequest req,
                                                 @RequestHeader("Authorization") String jwt) throws Exception{
        CartItem cartItem=cartService.addItemToCart(req, jwt);
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem>updateCartItemQuantity(@RequestBody UpdateCardItemRequest req,
                                                          @RequestHeader("Authorization") String jwt) throws Exception{
        CartItem cartItem=cartService.updateCartItemQuantity(req.getCardItemId(), req.getQuantity());
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart>removeCardItem(@PathVariable Long id,
                                              @RequestHeader("Authorization") String jwt) throws Exception{
        Cart cart=cartService.removeItemFromCart(id, jwt);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }


    @PutMapping("/cart/clear")
    public ResponseEntity<Cart>clearCart(@RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.clearCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/cart")
    public ResponseEntity<Cart>findUserCart(@RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.findCartByUserId(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

}
