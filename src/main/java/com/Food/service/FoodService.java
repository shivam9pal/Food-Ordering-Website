package com.Food.service;

import com.Food.models.Category;
import com.Food.models.Food;
import com.Food.models.Resturant;
import com.Food.request.CreateFoodRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FoodService {
    public Food createFood(CreateFoodRequest req, Category category, Resturant resturant);
    void deleteFood(Long foodId)throws Exception;
    public List<Food> getResturantsFood(Long resturantId,
                                        boolean isVegetarian,
                                        boolean isNonVeg,
                                        boolean isSeasional,
                                        String foodCategory
    );
    public List<Food> searchFood(String keyword);
    public Food findFoodById(Long foodId) throws  Exception;
    public Food updateAvailability(Long foodId) throws Exception;
}
