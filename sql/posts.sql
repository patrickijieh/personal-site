CREATE TABLE IF NOT EXISTS posts (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title varchar NOT NULL,
    post_body TEXT NOT NULL,
    created_by varchar REFERENCES dashboard_users (username) NOT NULL,
    created_at timestamp NOT NULL
);
