package com.Food.request;

import com.Food.models.Category;
import com.Food.models.IngredientsItem;
import lombok.Data;

import java.util.List;
@Data
public class CreateFoodRequest {

    private String name;
    private String description;
    private Long price;
    private Category category;
    private List<String> images;
    private Long resturantId;
    private Boolean vegetarian;
    private Boolean seasional;
    private List<IngredientsItem> ingredients;
}
