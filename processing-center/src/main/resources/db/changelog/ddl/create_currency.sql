-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_currency
CREATE TABLE IF NOT EXISTS currency
(
    id                    BIGSERIAL PRIMARY KEY UNIQUE,
    currency_digital_code VARCHAR(3),
    currency_letter_code  VARCHAR(3),
    currency_name         VARCHAR(255)
);
-- rollback DROP TABLE currency;