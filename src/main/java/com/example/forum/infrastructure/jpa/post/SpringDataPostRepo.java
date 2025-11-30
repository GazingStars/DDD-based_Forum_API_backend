package com.example.forum.infrastructure.jpa.post;

import com.example.forum.infrastructure.jpa.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataPostRepo extends JpaRepository<PostEntity, String> {
    List<PostEntity> findByCategoryId(String categoryId, Pageable pageable);
    @Query("""
        SELECT p FROM PostEntity p 
        WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%'))
           OR LOWER(p.content) LIKE LOWER(CONCAT('%', :q, '%'))
        ORDER BY p.createdAt DESC
    """)
    List<PostEntity> search(@Param("q") String q, Pageable pageable);
}
