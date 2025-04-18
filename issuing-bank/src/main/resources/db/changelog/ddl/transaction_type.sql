-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_transaction_type
create table if not exists transaction_type
(
    id                    bigserial primary key,
    transaction_type_name varchar(255)
)
-- rollback DROP TABLE transaction_type

-- changeset Yury Yashkov:tag_v_04
-- tag v_04
