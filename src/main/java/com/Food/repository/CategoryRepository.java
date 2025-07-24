package com.Food.repository;

import com.Food.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public List<Category> findByResturantId(Long id);
}
