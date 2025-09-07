-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_transaction
create table if not exists transaction
(
    id                              bigserial primary key,
    transaction_date                date,
    sum                             decimal,
    transaction_name                varchar(255),
    transaction_type_id             bigint references transaction_type on delete cascade,
    account_id                      bigint references account on delete cascade,
    sent_to_processing_center       timestamp,
    received_from_processing_center timestamp
)
-- rollback DROP TABLE transaction

-- changeset Yury Yashkov:tag_v_10
-- tag v_10
