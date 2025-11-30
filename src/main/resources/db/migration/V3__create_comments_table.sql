CREATE TABLE comments (
    id VARCHAR(36) PRIMARY KEY,

    post_id VARCHAR(36) NOT NULL,
    author_id VARCHAR(36) NOT NULL,

    content TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_comments_post
        FOREIGN KEY (post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_comments_author
        FOREIGN KEY (author_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);