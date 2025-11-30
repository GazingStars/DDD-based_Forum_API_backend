package com.example.forum.infrastructure.jpa.post;

import com.example.forum.domain.model.category.CategoryId;
import com.example.forum.domain.model.post.Post;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaPostRepository implements PostRepository {

    private final SpringDataPostRepo repository;


    public JpaPostRepository(SpringDataPostRepo repository) {
        this.repository = repository;
    }

    @Override
    public Post save(Post post) {
        PostEntity saved = repository.save(PostMapper.toEntity(post));
        return PostMapper.toDomain(saved);
    }

    @Override
    public Optional<Post> findPostById(PostId id) {
        return repository.findById(id.value()).map(PostMapper::toDomain);
    }

    @Override
    public List<Post> findAllPaged(int page, int size) {
        return repository
                .findAll(PageRequest.of(page, size))
                .map(PostMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(PostId id) {
        if (!repository.existsById(id.value())) {
            throw new IllegalArgumentException("Post not found: " + id.value());
        }

        repository.deleteById(id.value());
    }

    @Override
    public List<Post> search(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.search(query, pageable)
                .stream()
                .map(PostMapper::toDomain)
                .toList();
    }

    @Override
    public List<Post> findByCategoryPaged(CategoryId categoryId, int page, int size) {
        return repository
                .findByCategoryId(categoryId.value(), PageRequest.of(page, size))
                .stream()
                .map(PostMapper::toDomain)
                .toList();
    }
}
