package com.example.forum.infrastructure.jpa.comment;

import com.example.forum.domain.model.comment.Comment;
import com.example.forum.domain.model.comment.CommentContent;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;


public class CommentMapper {

    public static CommentEntity toEntity(Comment comment) {
        return new CommentEntity(
                comment.id().value(),
                comment.postId().value(),
                comment.authorId().get(),
                comment.content().value(),
                comment.createdAt(),
                comment.updatedAt()
        );
    }


    public static Comment toDomain(CommentEntity entity) {
        return new Comment(
                new CommentId(entity.getId()),
                new PostId(entity.getPostId()),
                new UserId(entity.getAuthorId()),
                new CommentContent(entity.getContent()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
