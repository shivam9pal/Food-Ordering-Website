package com.Food.service;

import com.Food.models.Category;

import java.util.List;

public interface CategoryService {

    public Category createCategory(String name,Long userId) throws Exception;
    public List<Category> findCategoryByResturantId(Long id) throws Exception;
    public Category findCategoryById(Long id) throws Exception;
}
