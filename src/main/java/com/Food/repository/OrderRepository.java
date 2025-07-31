package com.Food.repository;


import com.Food.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    public List<Order> findByCustomerId(Long userId);
    public List<Order> findByResturantId(Long resturantId);
}
