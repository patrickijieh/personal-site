CREATE TABLE IF NOT EXISTS dashboard_users (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username varchar NOT NULL CONSTRAINT must_be_unique UNIQUE,
    password varchar NOT NULL
);
