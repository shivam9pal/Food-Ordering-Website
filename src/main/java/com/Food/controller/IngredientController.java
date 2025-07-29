package com.Food.controller;


import com.Food.models.IngredientsCategory;
import com.Food.models.IngredientsItem;
import com.Food.request.IngredientCategoryRequest;
import com.Food.request.IngredientRequest;
import com.Food.service.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/ingredients")
public class IngredientController {

    @Autowired
    private IngredientsService ingredientsService;

    @PostMapping("category")
    public ResponseEntity<IngredientsCategory> createIngredientCategory(
            @RequestBody IngredientCategoryRequest req) throws Exception{
            IngredientsCategory item=ingredientsService.createIngredientCategory(req.getName(), req.getResturantId());
            return new ResponseEntity<>(item,HttpStatus.CREATED);
    }

    @PostMapping()
    public ResponseEntity<IngredientsItem> createIngredientItem(
            @RequestBody IngredientRequest req) throws Exception{
        IngredientsItem item=ingredientsService.createIngredientItem(req.getResturantId(), req.getName(), req.getCategoryId());
        return new ResponseEntity<>(item,HttpStatus.CREATED);
    }

    @PostMapping("/{id}/stock")
    public ResponseEntity<IngredientsItem> updateIngredientStock(
            @PathVariable Long id) throws Exception{
        IngredientsItem item=ingredientsService.updateStock(id);
        return new ResponseEntity<>(item,HttpStatus.CREATED);
    }

    @GetMapping("/resturant/{id}")
    public ResponseEntity<List<IngredientsItem>> getResturantIngredient(
            @PathVariable Long id) throws Exception{
        List<IngredientsItem> item=ingredientsService.findResturantsIngredient(id);
        return new ResponseEntity<>(item,HttpStatus.CREATED);
    }

    @GetMapping("/resturant/{id}/category")
    public ResponseEntity<List<IngredientsCategory>> getResturantIngredientCategory(
            @PathVariable Long id) throws Exception{
        List<IngredientsCategory> item=ingredientsService.findIngredientCategoryByResturantId(id);
        return new ResponseEntity<>(item,HttpStatus.CREATED);
    }
}
