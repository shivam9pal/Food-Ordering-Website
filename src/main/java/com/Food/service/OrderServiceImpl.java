package com.Food.service;

import com.Food.models.*;
import com.Food.repository.*;
import com.Food.request.OrderRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResturantService resturantService;
    @Autowired
    private CartService cartService;


    @Override
    public Order createOrder(OrderRequest order, User user) throws Exception {
        Address shipAddress=order.getDeliveryAddress();
        Address saveAddress=addressRepository.save(shipAddress);
        if(!user.getAdresses().contains(saveAddress)){
            user.getAdresses().add(saveAddress);
            userRepository.save(user);
        }
        Resturant resturant=resturantService.findResturantById(order.getResturantId());
        Order createOrder=new Order();
        createOrder.setCustomer(user);
        createOrder.setCreatedAt(new Date());
        createOrder.setOrderStatus("Pending");
        createOrder.setDeliveryAddress(saveAddress);
        createOrder.setResturant(resturant);

        Cart cart=cartService.findCartByUserId(user.getId());
        List<OrderItem> orderItems=new ArrayList<>();
        for(CartItem cartItem:cart.getItem()){
            OrderItem orderItem=new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedOrderItem=orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }

        Long totalPrice= cartService.calculateCartTotal(cart);

        createOrder.setItems(orderItems);
        createOrder.setTotalPrice(totalPrice);

        Order savedOrder=orderRepository.save(createOrder);
        resturant.getOrders().add(savedOrder);

        return createOrder ;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        Order order=findOrderById(orderId);
        if(orderStatus.equals("OUT_FOR_DELIVERY")||
                orderStatus.equals("DELIVERED")||
                orderStatus.equals("COMPLETED")||
                orderStatus.equals("PENDING")){
            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        throw new Exception("Please Select Valid Order Status");

    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
        Order order=findOrderById(orderId);
        orderRepository.delete(order);

    }

    @Override
    public List<Order> getUsersOrder(Long userId) throws Exception {
        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public List<Order> getResturantsOrder(Long resturantId, String orderStatus) throws Exception {
        List<Order> orders=orderRepository.findByResturantId(resturantId);
        if(orderStatus!=null){
            orders= orders.stream().filter(order->
                    order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> optionalOrder=orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new Exception("order not found");
        }
        return optionalOrder.get();
    }
}
