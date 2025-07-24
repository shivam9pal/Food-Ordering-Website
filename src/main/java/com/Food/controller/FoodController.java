package com.Food.controller;


import com.Food.models.Food;
import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.request.CreateFoodRequest;
import com.Food.service.FoodService;
import com.Food.service.ResturantService;
import com.Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {


    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResturantService resturantService;

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
                                           @RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        List<Food> food=foodService.searchFood(name);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @GetMapping("/resturant/{resturantId}")
    public ResponseEntity<List<Food>> getResturantFood(@RequestParam boolean vegetarian,
                                                       @RequestParam boolean seasonal ,
                                                       @RequestParam boolean nonveg,
                                                       @RequestParam(required = false) String food_category,
                                                       @PathVariable Long resturantId,
                                                       @RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        List<Food> food=foodService.getResturantsFood(resturantId,vegetarian,nonveg,seasonal,food_category);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }
}
