CREATE TABLE IF NOT EXISTS posts (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    post_body TEXT NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_at timestamp NOT NULL
);
