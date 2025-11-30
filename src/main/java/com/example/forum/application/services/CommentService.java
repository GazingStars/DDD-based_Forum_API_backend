package com.example.forum.application.services;

import com.example.forum.application.DTOs.comment.CreateCommentRequest;
import com.example.forum.application.DTOs.comment.UpdateCommentRequest;
import com.example.forum.domain.model.comment.Comment;
import com.example.forum.domain.model.comment.CommentContent;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.Role;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository comments;

    public CommentService(CommentRepository comments) {
        this.comments = comments;
    }

    @Transactional
    public Comment createComment(PostId postId, UserId author, CreateCommentRequest request) {

        Comment comment = new Comment(
                CommentId.newId(),
                postId,
                author,
                new CommentContent(request.content())
        );

        return comments.save(comment);
    }

    @Transactional
    public Comment editComment(CommentId id, UserId editor, UpdateCommentRequest request) {

        Comment comment = comments.findById(id).orElseThrow();

        comment.edit(editor, new CommentContent(request.content()));

        return comments.save(comment);
    }

    @Transactional
    public void deleteComment(CommentId id, UserId user, Role role) {

        Comment comment = comments.findById(id).orElseThrow();

        comment.delete(user, role);

        comments.delete(comment);
    }

    @Transactional
    public List<Comment> getCommentsPaged(PostId postId, int page, int size) {
        return comments.getPagedByPost(postId, page, size);
    }

}
