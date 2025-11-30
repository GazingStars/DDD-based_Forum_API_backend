package com.example.forum.infrastructure.jpa.category;


import com.example.forum.domain.model.category.*;

public class CategoryMapper {

    public static Category toDomain(CategoryEntity e) {
        return new Category(
                new CategoryId(e.getId()),
                new CategoryName(e.getName()),
                e.getDescription(),
                new CategorySlug(e.getSlug()),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }

    public static CategoryEntity toEntity(Category category) {
        return new CategoryEntity(
                category.id().value(),
                category.name().value(),
                category.slug().value(),
                category.description(),
                category.createdAt(),
                category.updatedAt()
        );
    }
}
