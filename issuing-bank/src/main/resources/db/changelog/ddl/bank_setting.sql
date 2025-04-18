-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_bank_setting
create table if not exists bank_setting
(
    id            bigserial primary key,
    bank_setting  varchar(100),
    current_value varchar(255),
    description   varchar(255)
)
-- rollback DROP TABLE bank_setting

-- changeset Yury Yashkov:tag_v_05
-- tag v_05
