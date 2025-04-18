-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_currency
create table if not exists currency
(
    id                            bigserial primary key,
    currency_digital_code         varchar(3),
    currency_letter_code          varchar(3),
    currency_digital_code_account varchar(3),
    currency_name                 varchar(255)
)
-- rollback DROP TABLE currency

-- changeset Yury Yashkov:tag_v_03
-- tag v_03
