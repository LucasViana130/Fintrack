CREATE TABLE accounts (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT              NOT NULL REFERENCES users(id),
    name        VARCHAR(100)        NOT NULL,
    type        VARCHAR(20)         NOT NULL,
    description VARCHAR(255),
    active      BOOLEAN             NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP           NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_accounts_user_id ON accounts(user_id);
