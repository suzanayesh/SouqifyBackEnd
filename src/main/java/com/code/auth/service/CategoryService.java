package com.code.auth.service;

import com.code.auth.dto.user.CategoryDto;
import com.code.auth.entity.Category;
import com.code.auth.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName(), category.getImageUrl());
    }

    // Method to create a new category
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setImageUrl(categoryDto.getImageUrl());
        // Optionally set the parent category if your model supports hierarchical categories
        // if (categoryDto.getParentCategoryId() != null) {
        //     Category parent = categoryRepository.findById(categoryDto.getParentCategoryId()).orElseThrow();
        //     category.setParentCategory(parent);
        // }
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    // Method to update an existing category
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        category.setName(categoryDto.getName());
        category.setImageUrl(categoryDto.getImageUrl());
        // Optionally update the parent category if needed
        // if (categoryDto.getParentCategoryId() != null) {
        //     Category parent = categoryRepository.findById(categoryDto.getParentCategoryId()).orElseThrow();
        //     category.setParentCategory(parent);
        // }
        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }

    // Method to delete a category by its ID
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}





//package com.code.auth.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import com.code.auth.entity.Category;
//import com.code.auth.repo.CategoryRepository;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class CategoryService {
//
//    private final CategoryRepository categoryRepository;
//
//    public CategoryService(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
//
//    public List<Category> findAllCategories() {
//        return categoryRepository.findAll();
//    }
//
//    public Optional<Category> findCategoryById(Long id) {
//        return categoryRepository.findById(id);
//    }
//
//    public Category saveCategory(Category category) {
//        return categoryRepository.save(category);
//    }
//
//    public void deleteCategory(Long id) {
//        categoryRepository.deleteById(id);
//    }
//    public Category updateCategoryParent(Long categoryId, Long newParentId) {
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
//
//        if (newParentId != null) {
//            Category newParent = categoryRepository.findById(newParentId)
//                    .orElseThrow(() -> new RuntimeException("New parent category not found with id: " + newParentId));
//            category.setParentCategory(newParent);
//        } else {
//            category.setParentCategory(null); // Or handle as needed for detaching from parent
//        }
//
//        return categoryRepository.save(category);
//    }
//
//}
