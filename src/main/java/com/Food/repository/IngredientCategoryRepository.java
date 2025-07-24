package com.Food.repository;

import com.Food.models.Category;
import com.Food.models.IngredientsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientCategoryRepository extends JpaRepository<IngredientsCategory,Long> {
    List<IngredientsCategory> findResturantById(Long id);
}
