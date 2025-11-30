package com.example.forum.domain.repository;

import com.example.forum.domain.model.comment.Comment;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.post.PostId;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(CommentId id);

    List<Comment> findByPostId(PostId postId);

    void delete(Comment comment);

    List<Comment> getPagedByPost(PostId postId, int page, int size);

}
