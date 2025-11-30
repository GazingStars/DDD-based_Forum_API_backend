package com.example.forum.infrastructure.jpa.comment;

import com.example.forum.domain.model.comment.Comment;
import com.example.forum.domain.model.comment.CommentContent;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.repository.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCommentRepository implements CommentRepository {

    private final SpringDataCommentRepo repository;

    JpaCommentRepository(SpringDataCommentRepo repository) {
        this.repository = repository;
    }

    @Override
    public Comment save(Comment comment) {
        var saved = repository.save(CommentMapper.toEntity(comment));
        return CommentMapper.toDomain(saved);
    }

    @Override
    public Optional<Comment> findById(CommentId id) {
        return repository.findById(id.value()).map(CommentMapper::toDomain);
    }

    @Override
    public List<Comment> findByPostId(PostId postId) {
        return repository.findByPostId(postId.value()).stream().map(CommentMapper::toDomain).toList();
    }

    @Override
    public void delete(Comment comment) {
        repository.deleteById(comment.id().value());
    }

    @Override
    public List<Comment> getPagedByPost(PostId postId, int page, int size) {
        var pageable = PageRequest.of(page, size);

        return repository
                .findByPostId(postId.value(), pageable)
                .stream()
                .map(CommentMapper::toDomain)
                .toList();
    }
}
