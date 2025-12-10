package com.example.forum.application.services;

import com.example.forum.application.DTOs.post.CreatePostRequest;
import com.example.forum.application.DTOs.post.UpdatePostRequest;
import com.example.forum.domain.model.category.CategoryId;
import com.example.forum.domain.model.category.CategorySlug;
import com.example.forum.domain.model.post.Post;
import com.example.forum.domain.model.post.PostContent;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.Role;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.repository.CategoryRepository;
import com.example.forum.domain.repository.PostRepository;
import com.example.forum.domain.service.PostDomainService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;
    private final PostDomainService domainService;
    private final CategoryRepository categories;

    public PostService(PostRepository repository, PostDomainService service, CategoryRepository categories) {
        this.repository = repository;
        this.domainService = service;
        this.categories = categories;
    }

    @Transactional
    public Post createPost(UserId authorId, CreatePostRequest dto) {

        Post post = Post.create(
                authorId,
                dto.title(),
                new PostContent(dto.content()),
                new CategoryId(dto.categoryId())
        );

        return repository.save(post);
    }

    @Transactional
    public Post getPost(PostId id) {
        return repository.findPostById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Transactional
    public List<Post> getByCategorySlug(String slug, int page, int size) {

        CategorySlug categorySlug = new CategorySlug(slug);

        var category = categories.findBySlug(categorySlug)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        CategoryId categoryId = category.id();

        return repository.findByCategoryPaged(categoryId, page, size);
    }

    @Transactional
    public List<Post> searchPosts(String query, int page, int size) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return repository.search(query, page, size);
    }

    @Transactional
    public List<Post> getPaged(int page, int size) {
        return repository.findAllPaged(page, size);
    }

    @Transactional
    public Post editPost(PostId postId, UserId editorId, Role editorRole, UpdatePostRequest req) {

        Post post = repository.findPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean isOwner = post.getAuthorId().equals(editorId);
        boolean isModerator = editorRole == Role.MODERATOR;

        if (!isOwner && !isModerator) {
            throw new RuntimeException("Forbidden");
        }

        CategoryId categoryToApply = post.getCategoryId();

        if (req.categoryId() != null) {
            categoryToApply = new CategoryId(req.categoryId());
        }

        post.edit(
                req.title(),
                new PostContent(req.content()),
                categoryToApply
        );

        return repository.save(post);
    }

    @Transactional
    public Post pinPost(PostId id, Role actorRole) {
        if (actorRole != Role.ADMIN && actorRole != Role.MODERATOR) {
            throw new RuntimeException("Forbidden");
        }

        Post post = getPost(id);
        post.pin();
        return repository.save(post);
    }

    @Transactional
    public Post lockPost(PostId id, Role role) {
        if (role != Role.ADMIN && role != Role.MODERATOR)
            throw new RuntimeException("Forbidden");

        Post post = getPost(id);
        post.lock();
        return repository.save(post);
    }

    @Transactional
    public List<Post> getByUser(UserId userId) {
        return repository.findByUser(userId);
    }

    @Transactional
    public void deletePost(PostId id, UserId executor, Role role) {

        Post post = getPost(id);

        if (!domainService.canDelete(post, executor, role)) {
            throw new RuntimeException("Forbidden");
        }

        repository.delete(id);
    }
}
