package com.code.auth.controller;

import com.code.auth.dto.user.CategoryDto;
import com.code.auth.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.findAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }

    // Update an existing category
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    // Delete a category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();  // You could also return ResponseEntity.noContent().build();
    }
}




//package com.code.auth.controller;
//
//
//import com.code.auth.entity.Category;
//import com.code.auth.service.CategoryService;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/categories")
//public class CategoryController {
//
//    private final CategoryService categoryService;
//
//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }
//
//    @GetMapping("getAll")
//    public List<Category> getAllCategories() {
//        return categoryService.findAllCategories();
//    }
//
////    @GetMapping("/{id}")
////    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
////        return categoryService.findCategoryById(id)
////                .map(ResponseEntity::ok)
////                .orElseGet(() -> ResponseEntity.notFound().build());
////    }
//
//    @PostMapping("add")
//    @PreAuthorize("hasAuthority('ADMIN_PER')")
//    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
//        try {
//            Category savedCategory = categoryService.saveCategory(category);
//            return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
//        } catch (DataIntegrityViolationException e) {
//            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
//        }
//    }
//
//    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN_PER')")
//    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
//        Category existingCategory = categoryService.findCategoryById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
//
//        existingCategory.setName(categoryDetails.getName());
//        // Update other fields as necessary
//        final Category updatedCategory = categoryService.saveCategory(existingCategory);
//        return ResponseEntity.ok(updatedCategory);
//    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN_PER')")
//    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
//        categoryService.deleteCategory(id);
//        return ResponseEntity.ok("Category deleted successfully.");
//    }
//
//}
