package com.Food.controller;


import com.Food.models.Category;
import com.Food.models.User;
import com.Food.service.CategoryService;
import com.Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @PostMapping("/admin/category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Category createdCategory=categoryService.createCategory(category.getName(), user.getId());
        return  new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/category/resturant")
    public ResponseEntity<List<Category>> getResturantCategory(@RequestBody Category category,
                                                   @RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        List<Category> categories=categoryService.findCategoryByResturantId(user.getId());
        return  new ResponseEntity<>(categories, HttpStatus.CREATED);
    }
}
