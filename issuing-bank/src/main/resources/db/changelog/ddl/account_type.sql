-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_account_type
create table if not exists account_type
(
    id                bigserial primary key,
    account_type_name varchar(255)
)
-- rollback DROP TABLE account_type

-- changeset Yury Yashkov:tag_v_02
-- tag v_02
