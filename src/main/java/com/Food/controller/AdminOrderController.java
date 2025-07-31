package com.Food.controller;


import com.Food.models.Order;
import com.Food.models.User;
import com.Food.request.OrderRequest;
import com.Food.service.OrderService;
import com.Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;




    @GetMapping("/order/resturant/{id}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long id,@RequestParam(required = false) String order_status,
            @RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        List<Order> order =orderService.getResturantsOrder(id,order_status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/order/{id}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id,
                                                         @PathVariable String orderStatus,
                                                         @RequestParam(required = false) String order_status,
                                                       @RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Order order =orderService.updateOrder(id,orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
