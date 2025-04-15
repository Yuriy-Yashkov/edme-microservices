-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_issuing_bank
CREATE TABLE IF NOT EXISTS issuing_bank
(
    id               BIGSERIAL PRIMARY KEY UNIQUE,
    bic              VARCHAR(9),
    bin              VARCHAR(5),
    abbreviated_name VARCHAR(255)
);
-- rollback DROP TABLE issuing_bank
