-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_account
create table if not exists account
(
    id                    bigserial primary key,
    account_number        varchar(50),
    balance               decimal,
    currency_id           bigint references currency (id) on delete cascade,
    account_type_id       bigint references account_type (id) on delete cascade,
    client_id             bigint references client (id) on delete cascade,
    account_opening_date  date,
    suspending_operations boolean,
    account_closing_date  date
)
-- rollback DROP TABLE account

-- changeset Yury Yashkov:tag_v_08
-- tag v_08
