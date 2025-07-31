package com.Food.service;

import com.Food.models.Category;
import com.Food.models.Resturant;
import com.Food.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryServiceImp implements CategoryService{

    @Autowired
    private ResturantService resturantService;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Category createCategory(String name, Long userId) throws Exception {
        Resturant resturant=resturantService.getResturantByUserId(userId);
        Category category=new Category();
        category.setName(name);
        category.setResturant(resturant);

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoryByResturantId(Long id) throws Exception {
        Resturant resturant=resturantService.getResturantByUserId(id);
        return categoryRepository.findByResturantId(resturant.getId());
    }

    @Override
    public Category findCategoryById(Long id) throws Exception {
        Optional<Category> optionalCategory=categoryRepository.findById(id);
        if(optionalCategory.isEmpty()){
            throw new Exception("Category Not Found");
        }
        return optionalCategory.get();
    }
}
