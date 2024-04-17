package com.code.auth.service;

import java.util.List;
import java.util.Optional;

import com.code.auth.entity.Category;
import com.code.auth.repo.CategoryRepository;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    public Category updateCategoryParent(Long categoryId, Long newParentId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        if (newParentId != null) {
            Category newParent = categoryRepository.findById(newParentId)
                    .orElseThrow(() -> new RuntimeException("New parent category not found with id: " + newParentId));
            category.setParentCategory(newParent);
        } else {
            category.setParentCategory(null); // Or handle as needed for detaching from parent
        }

        return categoryRepository.save(category);
    }

}
