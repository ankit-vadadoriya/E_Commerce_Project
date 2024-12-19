package com.example.e_commerce1.service;

import com.example.e_commerce1.model.Category;
import com.example.e_commerce1.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
    public void addCategory(Category category){
        categoryRepository.save(category);
    }
    public void deleteCategoryById(int id){
        categoryRepository.deleteById(id);
    }
    public Optional<Category> findCategoryById(int id){
        return categoryRepository.findById(id);
    }
}
