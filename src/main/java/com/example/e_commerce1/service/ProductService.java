package com.example.e_commerce1.service;

import com.example.e_commerce1.model.Product;
import com.example.e_commerce1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    public void addProduct(Product product){
        productRepository.save(product);
    }
    public void deleteProductById(long id){
        productRepository.deleteById(id);
    }
    public Optional<Product> findProductById(long id){
        return productRepository.findById(id);
    }
    public List<Product> getALlProductsByCategory(int id){
        return productRepository.findAllByCategory_Id(id);
    }
}
