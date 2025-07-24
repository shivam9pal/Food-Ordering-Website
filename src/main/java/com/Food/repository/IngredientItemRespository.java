package com.Food.repository;

import com.Food.models.IngredientsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientItemRespository extends JpaRepository<IngredientsItem,Long> {
    List<IngredientsItem> findByResturantId(Long id);
}
