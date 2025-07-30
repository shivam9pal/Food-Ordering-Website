package com.Food.service;

import com.Food.models.Cart;
import com.Food.models.CartItem;
import com.Food.models.Food;
import com.Food.models.User;
import com.Food.repository.CartItemRepository;
import com.Food.repository.CartRepository;
import com.Food.repository.FoodRepository;
import com.Food.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService
{
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private FoodService foodService;


    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Food food=foodService.findFoodById(req.getFoodId());
        Cart cart=cartRepository.findByCustomerId(user.getId());

        for(CartItem cartItem: cart.getItem()){
            if(cartItem.getFood().equals(food)){
                int newQuantity=cartItem.getQuantity()+ req.getQuantity();
                return updateCartItemQuantity(cart.getId(),newQuantity);
            }

        }
        CartItem newCartItem=new CartItem();
        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setIngredients(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity()*food.getPrice());
        CartItem saveCartItem=cartItemRepository.save(newCartItem);
        cart.getItem().add(saveCartItem);
            return saveCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        Optional<CartItem> cartItemOptional=cartItemRepository.findById(cartItemId);
        if(cartItemOptional.isEmpty()){
            throw new Exception("Cart item Not found");
        }
        CartItem item=cartItemOptional.get();
        item.setQuantity(quantity);
        item.setTotalPrice(item.getFood().getPrice()*quantity);
        return cartItemRepository.save(item);
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);

        Cart cart=cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> cartItemOptional=cartItemRepository.findById(cartItemId);
        if(cartItemOptional.isEmpty()){
            throw new Exception("Card item not found");
        }
        CartItem item=cartItemOptional.get();
        cart.getItem().remove(item);
        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
        Long total=0L;
        for(CartItem cartItem: cart.getItem()){
            total+=cartItem.getFood().getPrice()*cartItem.getQuantity();
        }
        return total;
    }

    @Override
    public Cart findCartById(Long id) throws Exception {
        Optional<Cart> optionalCart=cartRepository.findById(id);
        if(optionalCart.isEmpty()){
            throw new Exception("Card item not found  with id"+id);
        }
        return optionalCart.get();
    }

    @Override
    public Cart findCartByUserId(Long UserId) throws Exception {
        return cartRepository.findByCustomerId(UserId);
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
        Cart cart=findCartById(userId);
        cart.getItem().clear();
        return cartRepository.save(cart);
    }
}
