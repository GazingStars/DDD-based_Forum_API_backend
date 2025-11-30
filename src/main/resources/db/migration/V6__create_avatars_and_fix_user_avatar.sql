
CREATE TABLE IF NOT EXISTS avatars (
    id VARCHAR(36) PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    url TEXT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'users'
          AND column_name = 'avatar_id'
    ) THEN
        ALTER TABLE users ADD COLUMN avatar_id VARCHAR(36);
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'users'
          AND column_name = 'avatar_url'
    ) THEN
        ALTER TABLE users DROP COLUMN avatar_url;
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE table_name = 'users'
          AND constraint_name = 'fk_user_avatar'
    ) THEN
        ALTER TABLE users
            ADD CONSTRAINT fk_user_avatar
                FOREIGN KEY (avatar_id)
                REFERENCES avatars(id);
    END IF;
END $$;
