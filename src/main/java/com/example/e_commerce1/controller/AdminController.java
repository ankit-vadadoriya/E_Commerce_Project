package com.example.e_commerce1.controller;

import com.example.e_commerce1.dto.ProductDTO;
import com.example.e_commerce1.model.Category;
import com.example.e_commerce1.model.Product;
import com.example.e_commerce1.service.CategoryService;
import com.example.e_commerce1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {
    public String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }
    @GetMapping("/admin/categories")
    public String getCategories(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        return "categories";
    }
    @GetMapping("/admin/categories/add")
    public String getCategoriesAdd(Model model){
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }
    @PostMapping("/admin/categories/add")
    public String postCategoriesAdd(@ModelAttribute("category") Category category){
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id){
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model){
        Optional<Category> category = categoryService.findCategoryById(id);
        if (category.isPresent()){
            model.addAttribute("category", category.get());
            return "categoriesAdd";
        } else {
            return "404";
        }
    }

    //Product Section

    @GetMapping("/admin/products")
    public String getProducts(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "products";
    }
    @GetMapping("/admin/products/add")
    public String addProducts(Model model){
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }
    @PostMapping("/admin/products/add")
    public String postProductAdd(@ModelAttribute("productDTO")ProductDTO productDTO,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName")String imgName) throws IOException{
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.findCategoryById(productDTO.getCategoryId()).get());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDescription(productDTO.getDescription());
        String imageUUID;
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            if (!file.isEmpty()) {
                imageUUID = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
                Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
                Files.write(fileNameAndPath, file.getBytes());
                System.out.println("File saved successfully to: " + fileNameAndPath);
            } else {
                imageUUID = imgName;
            }
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("File upload failed : " + e.getMessage());
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id, Model model){
        Product product = productService.findProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productDTO", productDTO);

        return "productsAdd";
    }
}
