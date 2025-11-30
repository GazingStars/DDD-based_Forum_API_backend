package com.example.forum.infrastructure.jpa.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpringDataCommentRepo extends JpaRepository<CommentEntity, String> {

    List<CommentEntity> findByPostId(String postId);

    Page<CommentEntity> findByPostId(String postId, Pageable pageable);
}
