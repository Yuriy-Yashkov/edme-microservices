-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_client
create table if not exists client
(
    id          bigserial primary key,
    last_name   varchar(100),
    first_name  varchar(100),
    middle_name varchar(100),
    birth_date  date,
    document    varchar(255),
    address     varchar(255),
    phone       varchar(20),
    email       varchar(255)
)
-- rollback DROP TABLE client

-- changeset Yury Yashkov:tag_v_01
-- tag v_01
