-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_transaction
create table if not exists transaction
(
    id                         bigserial primary key unique,
    transaction_date           date,
    sum                        decimal,
    transaction_name           varchar(255),
    account_id                 bigint,
    transaction_type_id        bigint,
    card_id                    bigint,
    terminal_id                bigint,
    response_code_id           bigint,
    authorization_code         varchar(6),
    received_from_issuing_bank timestamp,
    sent_to_issuing_bank       timestamp,
    foreign key (account_id) references account (id) on delete cascade,
    foreign key (transaction_type_id) references transaction_type (id) on delete cascade,
    foreign key (card_id) references card (id) on delete cascade,
    foreign key (terminal_id) references terminal (id) on delete cascade,
    foreign key (response_code_id) references response_code (id) on delete cascade
);
-- rollback DROP TABLE transaction
