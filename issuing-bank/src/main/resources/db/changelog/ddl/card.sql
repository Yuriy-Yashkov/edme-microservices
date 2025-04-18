-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_card
create table if not exists card
(
    id                              bigserial primary key,
    card_number                     varchar(50),
    expiration_date                 date,
    holder_name                     varchar(50),
    card_status_id                  bigint references card_status (id) on delete cascade,
    payment_system_id               bigint references payment_system (id) on delete cascade,
    account_id                      bigint references account (id) on delete cascade,
    client_id                       bigint references client (id) on delete cascade,
    sent_to_processing_center       timestamp,
    received_from_processing_center timestamp
)
-- rollback DROP TABLE card

-- changeset Yury Yashkov:tag_v_09
-- tag v_09
