package com.example.e_commerce1.repository;

import com.example.e_commerce1.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
