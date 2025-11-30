package com.example.forum.domain.repository;

import com.example.forum.domain.model.category.*;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(CategoryId id);

    Optional<Category> findBySlug(CategorySlug slug);

    List<Category> findAll();

    void delete(CategoryId id);

    boolean existsBySlug(CategorySlug slug);
}
