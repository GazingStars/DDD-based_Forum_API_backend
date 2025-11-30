CREATE TABLE avatars (
    id VARCHAR(36) PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL
);

ALTER TABLE users
    ADD COLUMN avatar_id VARCHAR(36);

ALTER TABLE users
    ADD CONSTRAINT fk_user_avatar
        FOREIGN KEY (avatar_id)
        REFERENCES avatars(id);