package com.Food.service;


import com.Food.models.IngredientsCategory;
import com.Food.models.IngredientsItem;
import com.Food.models.Resturant;
import com.Food.repository.IngredientCategoryRepository;
import com.Food.repository.IngredientItemRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImp implements IngredientsService{
    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository;
    @Autowired
    private IngredientItemRespository ingredientItemRespository;
    @Autowired
    private ResturantService resturantService;


    @Override
    public IngredientsCategory createIngredientCategory(String name, Long resturantId) throws Exception {
        Resturant resturant=resturantService.findResturantById(resturantId);
        IngredientsCategory category=new IngredientsCategory();
        category.setResturant(resturant);
        category.setName(name);
        return ingredientCategoryRepository.save(category);
    }

    @Override
    public IngredientsCategory findIngredientCategoryById(Long id) throws Exception {
        Optional<IngredientsCategory> opt=ingredientCategoryRepository.findById(id);
        if(opt.isEmpty()){
            throw new Exception("Category not found");
        }
        return opt.get();
    }

    @Override
    public List<IngredientsCategory> findIngredientCategoryByResturantId(Long id) throws Exception {
        resturantService.findResturantById(id);
        return ingredientCategoryRepository.findResturantById(id);
    }

    @Override
    public IngredientsItem createIngredientItem(Long resturantId, String ingredientName, Long categoryId) throws Exception {
        Resturant resturant=resturantService.findResturantById(resturantId);
        IngredientsCategory category=findIngredientCategoryById(categoryId);

        IngredientsItem item=new IngredientsItem();
        item.setName(ingredientName);
        item.setResturant(resturant);
        item.setCategory(category);

        IngredientsItem ingredient=ingredientItemRespository.save(item);
        category.getIngredients().add(ingredient);
        return ingredient;
    }

    @Override
    public List<IngredientsItem> findResturantsIngredient(Long resturantId) {
        return ingredientItemRespository.findByResturantId(resturantId);
    }

    @Override
    public IngredientsItem updateStock(Long id) throws Exception {
        Optional<IngredientsItem> optionalIngredientsItem=ingredientItemRespository.findById(id);
        if(optionalIngredientsItem.isEmpty()){
            throw new Exception("Ingredient not found");
        }
        IngredientsItem ingredientsItem=optionalIngredientsItem.get();
        ingredientsItem.setInStock(!ingredientsItem.isInStock());
        return ingredientItemRespository.save(ingredientsItem);
    }
}
