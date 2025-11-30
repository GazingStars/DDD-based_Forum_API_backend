package com.example.forum.infrastructure.jpa.post;

import com.example.forum.domain.model.category.CategoryId;
import com.example.forum.domain.model.post.Post;
import com.example.forum.domain.model.post.PostContent;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;

public class PostMapper {

    public static PostEntity toEntity(Post post) {
        return new PostEntity(
                post.getId().value(),
                post.getAuthorId().get(),
                post.getTitle(),
                post.getContent().value(),
                post.getCategoryId().value(),
                post.isPinned(),
                post.isLocked(),
                post.getCreatedAt(),
                post.getEditedAt()
        );
    }

    public static Post toDomain(PostEntity entity) {
        return new Post(
                new PostId(entity.getId()),
                new UserId(entity.getAuthorId()),
                entity.getTitle(),
                new PostContent(entity.getContent()),
                new CategoryId(entity.getCategoryId()),
                entity.isPinned(),
                entity.isLocked(),
                entity.getCreatedAt(),
                entity.getEditedAt()
        );
    }
}
