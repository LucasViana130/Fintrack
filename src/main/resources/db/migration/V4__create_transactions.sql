CREATE TABLE transactions (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT          NOT NULL REFERENCES users(id),
    account_id  BIGINT          NOT NULL REFERENCES accounts(id),
    category_id BIGINT          NOT NULL REFERENCES categories(id),
    type        VARCHAR(10)     NOT NULL,
    amount      NUMERIC(15, 2)  NOT NULL,
    description VARCHAR(255),
    date        DATE            NOT NULL,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_transactions_user_id    ON transactions(user_id);
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_date       ON transactions(date);
