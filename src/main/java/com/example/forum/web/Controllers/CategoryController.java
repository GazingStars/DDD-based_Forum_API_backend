package com.example.forum.infrastructure.web;

import com.example.forum.application.DTOs.category.CreateCategoryRequest;
import com.example.forum.application.DTOs.category.UpdateCategoryRequest;
import com.example.forum.application.DTOs.category.CategoryResponse;
import com.example.forum.application.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categories;

    public CategoryController(CategoryService categories) {
        this.categories = categories;
    }


    @GetMapping("/categories")
    public List<CategoryResponse> getAll() {
        return categories.getAll();
    }

    @GetMapping("/categories/{slug}")
    public CategoryResponse getBySlug(@PathVariable String slug) {
        return categories.getBySlug(slug);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponse> create(@RequestBody CreateCategoryRequest req) {
        CategoryResponse res = categories.create(req);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/admin/categories/{id}")
    public CategoryResponse update(
            @PathVariable String id,
            @RequestBody UpdateCategoryRequest req) {

        return categories.update(id, req);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categories.delete(id);
        return ResponseEntity.noContent().build();
    }
}
