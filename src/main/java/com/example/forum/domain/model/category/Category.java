package com.example.forum.domain.model.category;

import com.example.forum.domain.model.user.Role;
import com.example.forum.domain.model.user.UserId;

import java.time.Instant;

public class Category {

    private final CategoryId id;
    private CategoryName name;
    private String description;
    private CategorySlug slug;

    private final Instant createdAt;
    private Instant updatedAt;


    public Category(CategoryId id,
                    CategoryName name,
                    String description,
                    CategorySlug slug) {

        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;

        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static Category create(CategoryName name,
                                  String description,
                                  CategorySlug slug) {

        return new Category(
                CategoryId.generate(),
                name,
                description,
                slug
        );
    }

    public Category(CategoryId id,
                    CategoryName name,
                    String description,
                    CategorySlug slug,
                    Instant createdAt,
                    Instant updatedAt) {

        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void rename(CategoryName newName) {
        this.name = newName;
        this.updatedAt = Instant.now();
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = Instant.now();
    }

    public void changeSlug(CategorySlug newSlug) {
        this.slug = newSlug;
        this.updatedAt = Instant.now();
    }

    public CategoryId id() {
        return id;
    }

    public CategoryName name() {
        return name;
    }

    public String description() {
        return description;
    }

    public CategorySlug slug() {
        return slug;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
