-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_card_status
CREATE TABLE IF NOT EXISTS card_status
(
    id               BIGSERIAL PRIMARY KEY UNIQUE,
    card_status_name VARCHAR(255)
);
-- rollback DROP TABLE card_status;