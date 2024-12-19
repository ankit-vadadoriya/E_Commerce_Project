package com.example.e_commerce1.repository;

import com.example.e_commerce1.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory_Id(int id);
}
