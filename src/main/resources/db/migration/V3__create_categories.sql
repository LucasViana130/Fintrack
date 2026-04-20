CREATE TABLE categories (
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT      REFERENCES users(id),
    name    VARCHAR(100) NOT NULL,
    type    VARCHAR(10)  NOT NULL,
    color   VARCHAR(7),
    active  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_categories_user_id ON categories(user_id);
