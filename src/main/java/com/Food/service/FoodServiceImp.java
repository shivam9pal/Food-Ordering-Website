package com.Food.service;

import com.Food.models.Category;
import com.Food.models.Food;
import com.Food.models.Resturant;
import com.Food.repository.FoodRepository;
import com.Food.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImp implements FoodService{

    @Autowired
    private FoodRepository foodRepository;
    @Autowired


    @Override
    public Food createFood(CreateFoodRequest req, Category category, Resturant resturant) {
        Food food=new Food();
        food.setFoodCategory(category);
        food.setResturant(resturant);
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(resturant.getName());
        food.setPrice(req.getPrice());
        food.setIngredients(req.getIngredients());
        food.setSeasonal(req.getSeasional());
        food.setVegetarian(req.getVegetarian());
        Food savedFood= foodRepository.save(food);
        resturant.getFoods().add(savedFood);
        return savedFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {
        Food food=findFoodById(foodId);
        food.setResturant(null);
        foodRepository.save(food);
    }

    @Override
    public List<Food> getResturantsFood(Long resturantId,
                                        boolean isVegetarian,
                                        boolean isNonVeg,
                                        boolean isSeasional,
                                        String foodCategory) {
        List<Food> foods=foodRepository.findByResturantId(resturantId);
        if(isVegetarian){
            foods=filterByVegetarian(foods,isVegetarian);
        }
        if(isNonVeg){
            foods=filterByNonveg(foods,isNonVeg);
        }
        if(isSeasional){
            foods=filterBySeasonal(foods,isSeasional);
        }
        if(foodCategory!=null && !foodCategory.equals("")){
            foods=filterByCategory(foods,foodCategory);
        }

        return List.of();
    }

    private List<Food> filterByNonveg(List<Food> foods, boolean isNonVeg) {
        return foods.stream().filter(food-> food.isVegetarian()==false).collect(Collectors.toList());
    }

    private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasional) {
        return foods.stream().filter(food-> food.isSeasonal()==isSeasional).collect(Collectors.toList());
    }

    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream().filter(food ->{
            if(food.getFoodCategory()!=null){
                return food.getFoodCategory().getName().equals(foodCategory);
            }
            return false;
        }).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVegetarian) {
        return foods.stream().filter(food-> food.isVegetarian()==isVegetarian).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> food=foodRepository.findById(foodId);
        if(food.isEmpty()){
            throw new Exception("Food does not exist");
        }
        return food.get();
    }

    @Override
    public Food updateAvailability(Long foodId) throws Exception {
        Food food=findFoodById(foodId);
        food.setAvailable(!food.isAvailable());
        return foodRepository.save(food);
    }
}
