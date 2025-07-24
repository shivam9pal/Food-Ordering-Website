package com.Food.service;

import com.Food.models.IngredientsCategory;
import com.Food.models.IngredientsItem;

import java.util.List;

public interface IngredientsService {

    public IngredientsCategory createIngredientCategory(String name,Long resturantId)throws  Exception;

    public IngredientsCategory findIngredientCategoryById(Long id)throws Exception;

    public List<IngredientsCategory> findIngredientCategoryByResturantId(Long id)throws Exception;

    public IngredientsItem createIngredientItem(Long resturantId, String ingredientName, Long categoryId)throws Exception;

    public List<IngredientsItem> findResturantsIngredient(Long resturantId);

    public IngredientsItem updateStock(Long id)throws Exception;
}
