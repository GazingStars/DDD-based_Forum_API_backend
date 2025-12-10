package com.example.forum.domain.repository;

import com.example.forum.domain.model.category.CategoryId;
import com.example.forum.domain.model.post.Post;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    public Post save(Post post);
    public Optional<Post> findPostById(PostId id);
    public List<Post> findAllPaged(int page, int size);
    void delete(PostId id);
    List<Post> search(String query, int page, int size);
    List<Post> findByCategoryPaged(CategoryId categoryId, int page, int size);

    List<Post> findByUser(UserId userId);
}
