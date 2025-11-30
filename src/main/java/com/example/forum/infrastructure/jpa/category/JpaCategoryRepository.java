package com.example.forum.infrastructure.jpa.category;


import com.example.forum.domain.model.category.*;
import com.example.forum.domain.repository.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCategoryRepository implements CategoryRepository {

    private final SpringDataCategoryRepository jpa;

    public JpaCategoryRepository(SpringDataCategoryRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = CategoryMapper.toEntity(category);
        CategoryEntity saved = jpa.save(entity);
        return CategoryMapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return jpa.findById(id.value())
                .map(CategoryMapper::toDomain);
    }

    @Override
    public Optional<Category> findBySlug(CategorySlug slug) {
        return jpa.findBySlug(slug.value())
                .map(CategoryMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpa.findAll().stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(CategoryId id) {
        jpa.deleteById(id.value());
    }

    @Override
    public boolean existsBySlug(CategorySlug slug) {
        return jpa.existsBySlug(slug.value());
    }
}
