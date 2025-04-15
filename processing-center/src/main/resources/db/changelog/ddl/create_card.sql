-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_card
create table if not exists card
(
    id                         bigserial primary key unique,
    card_number                varchar(50),
    expiration_date            date,
    holder_name                varchar(50),
    card_status_id             bigint,
    payment_system_id          bigint,
    account_id                 bigint,
    received_from_issuing_bank timestamp,
    sent_to_issuing_bank       timestamp,
    foreign key (card_status_id) references card_status (id) on delete cascade,
    foreign key (payment_system_id) references payment_system (id) on delete cascade,
    foreign key (account_id) references account (id) on delete cascade
);
-- rollback DROP TABLE card
