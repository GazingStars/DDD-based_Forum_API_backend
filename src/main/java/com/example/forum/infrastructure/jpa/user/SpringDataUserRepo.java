package com.example.forum.infrastructure.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataUserRepo extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

    @Query("""
                SELECT u FROM UserEntity u 
                WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    List<UserEntity> search(@Param("q") String q);
}
