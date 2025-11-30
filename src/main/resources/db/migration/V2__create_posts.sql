CREATE TABLE posts (
    id VARCHAR(36) PRIMARY KEY,

    author_id VARCHAR(36) NOT NULL,

    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL,
    edited_at TIMESTAMP NULL,

    CONSTRAINT fk_posts_author
        FOREIGN KEY (author_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);