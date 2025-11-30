package com.example.forum.infrastructure.jpa.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, String> {
    Optional<CategoryEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
