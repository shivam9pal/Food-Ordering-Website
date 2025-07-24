package com.Food.controller;


import com.Food.models.Food;
import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.request.CreateFoodRequest;
import com.Food.response.MessageResponse;
import com.Food.service.FoodService;
import com.Food.service.ResturantService;
import com.Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food")
public class AdminFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResturantService resturantService;

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req,
                                           @RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Resturant resturant=resturantService.findResturantById(req.getResturantId());
        Food food=foodService.createFood(req,req.getCategory(),resturant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvailabilityStatus(@PathVariable Long id,
                                           @RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);

        Food food=foodService.updateAvailability(id);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwt) throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        foodService.deleteFood(id);
        MessageResponse msg=new MessageResponse();
        msg.setMessage("Food deleted Successfully");
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
}
