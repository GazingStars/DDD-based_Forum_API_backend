package com.example.forum.domain.model.post;

import com.example.forum.domain.model.category.CategoryId;
import com.example.forum.domain.model.user.UserId;

import java.time.Instant;

public class Post {

    private final PostId id;
    private final UserId authorId;

    private String title;
    private PostContent content;

    private CategoryId categoryId;
    private boolean pinned;
    private boolean locked;

    private final Instant createdAt;
    private Instant editedAt;

    public Post(PostId id,
                UserId authorId,
                String title,
                PostContent content,
                CategoryId categoryId) {

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be empty");

        if (content == null || content.value().isBlank())
            throw new IllegalArgumentException("Content cannot be empty");

        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;

        this.categoryId = categoryId;
        this.pinned = false;
        this.locked = false;

        this.createdAt = Instant.now();
        this.editedAt = null;
    }

    public static Post create(UserId authorId,
                              String title,
                              PostContent content,
                              CategoryId categoryId) {

        return new Post(
                PostId.generate(),
                authorId,
                title,
                content,
                categoryId
        );
    }

    public Post(PostId id,
                UserId authorId,
                String title,
                PostContent content,
                CategoryId categoryId,
                boolean pinned,
                boolean locked,
                Instant createdAt,
                Instant editedAt) {

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Title cannot be empty");

        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;

        this.categoryId = categoryId;
        this.pinned = pinned;
        this.locked = locked;

        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.editedAt = editedAt;
    }


    public void edit(String newTitle, PostContent newContent,CategoryId newCategory) {
        if (newTitle == null || newTitle.isBlank())
            throw new IllegalArgumentException("Title cannot be empty");

        if (newContent == null || newContent.value().isBlank())
            throw new IllegalArgumentException("Content cannot be empty");

        this.title = newTitle;
        this.content = newContent;
        this.editedAt = Instant.now();
        this.categoryId = newCategory;
    }

    public void assignCategory(CategoryId newCategory) {
        this.categoryId = newCategory;
        this.editedAt = Instant.now();
    }

    public void pin() { this.pinned = true; }
    public void unpin() { this.pinned = false; }

    public void lock() { this.locked = true; }
    public void unlock() { this.locked = false; }


    public PostId getId() { return id; }
    public UserId getAuthorId() { return authorId; }

    public String getTitle() { return title; }
    public PostContent getContent() { return content; }

    public CategoryId getCategoryId() { return categoryId; }
    public boolean isPinned() { return pinned; }
    public boolean isLocked() { return locked; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getEditedAt() { return editedAt; }
}
