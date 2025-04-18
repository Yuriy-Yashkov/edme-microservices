-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_card_status
create table if not exists card_status
(
    id               bigserial primary key,
    card_status_name varchar(255)
)
-- rollback DROP TABLE card_status

-- changeset Yury Yashkov:tag_v_06
-- tag v_06
