package com.example.forum.application.services;

import com.example.forum.application.DTOs.category.CreateCategoryRequest;
import com.example.forum.application.DTOs.category.UpdateCategoryRequest;
import com.example.forum.application.DTOs.category.CategoryResponse;
import com.example.forum.domain.model.category.*;
import com.example.forum.domain.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categories;

    public CategoryService(CategoryRepository categories) {
        this.categories = categories;
    }

    @Transactional
    public CategoryResponse create(CreateCategoryRequest req) {

        CategoryName name = new CategoryName(req.name());
        CategorySlug slug = new CategorySlug(req.slug());

        if (categories.existsBySlug(slug))
            throw new IllegalStateException("Category slug already exists");

        Category category = Category.create(
                name,
                req.description(),
                slug
        );

        categories.save(category);

        return toResponse(category);
    }

    @Transactional
    public CategoryResponse update(String id, UpdateCategoryRequest req) {

        CategoryId categoryId = new CategoryId(id);

        Category category = categories.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        if (req.name() != null)
            category.rename(new CategoryName(req.name()));

        if (req.description() != null)
            category.changeDescription(req.description());

        if (req.slug() != null) {
            CategorySlug newSlug = new CategorySlug(req.slug());

            if (categories.existsBySlug(newSlug) &&
                !category.slug().value().equals(req.slug()))
                throw new IllegalStateException("Slug already exists");

            category.changeSlug(newSlug);
        }

        categories.save(category);

        return toResponse(category);
    }

    @Transactional
    public void delete(String id) {
        categories.delete(new CategoryId(id));
    }


    public List<CategoryResponse> getAll() {
        return categories.findAll().stream()
                .map(this::toResponse)
                .toList();
    }


    public CategoryResponse getById(String id) {
        return categories.findById(new CategoryId(id))
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalStateException("Category not found"));
    }

    public CategoryResponse getBySlug(String slug) {
        return categories.findBySlug(new CategorySlug(slug))
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalStateException("Category not found"));
    }


    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(
                c.id().value(),
                c.name().value(),
                c.slug().value(),
                c.description(),
                c.createdAt(),
                c.updatedAt()
        );
    }
}
