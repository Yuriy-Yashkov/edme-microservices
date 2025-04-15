-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_account
create table if not exists account
(
    id              bigserial primary key unique,
    account_number  varchar(50),
    balance         decimal,
    currency_id     bigint references currency (id) on delete cascade,
    issuing_bank_id bigint references issuing_bank (id) on delete cascade
);
-- rollback DROP TABLE account
