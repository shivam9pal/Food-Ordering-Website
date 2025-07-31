package com.Food.service;

import com.Food.models.User;
import com.Food.request.OrderRequest;
import com.Food.models.Order;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface OrderService {
    public Order createOrder(OrderRequest order, User user) throws Exception;
    public Order updateOrder(Long orderId,String orderStatus) throws Exception;
    public void cancelOrder(Long orderId)throws Exception;
    public List<Order>getUsersOrder(Long userId) throws Exception;
    public List<Order>getResturantsOrder(Long resturantId,String orderStatus)throws Exception;
    public Order findOrderById(Long orderId)throws Exception;
}
