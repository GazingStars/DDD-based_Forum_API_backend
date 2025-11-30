ALTER TABLE users
    ADD COLUMN avatar_url TEXT NULL,
    ADD COLUMN is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN last_login_at TIMESTAMP NULL,
    ADD COLUMN updated_at TIMESTAMP NULL;

CREATE TABLE categories (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    description TEXT
);

ALTER TABLE posts
    ADD COLUMN category_id VARCHAR(36) NULL,
    ADD COLUMN is_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN updated_at TIMESTAMP NULL;

ALTER TABLE posts
    ADD CONSTRAINT fk_posts_category
        FOREIGN KEY (category_id) REFERENCES categories(id);

CREATE TABLE subscriptions (
    user_id VARCHAR(36) NOT NULL,
    post_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, post_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

