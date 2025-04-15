INSERT INTO card_status (id, card_status_name)
VALUES (1, 'Card is not active'),
       (2, 'Card is valid'),
       (3, 'Card is temporarily blocked'),
       (4, 'Card is lost'),
       (5, 'Card is compromised');
SELECT setval('card_status_id_seq', (SELECT MAX(id) FROM card_status));

INSERT INTO payment_system (id, payment_system_name)
VALUES (1, 'VISA International Service Association'),
       (2, 'Mastercard'),
       (3, 'JCB'),
       (4, 'American Express'),
       (5, 'Diners Club International'),
       (6, 'China UnionPay');
SELECT setval('payment_system_id_seq', (SELECT MAX(id) FROM payment_system));

INSERT INTO currency (id, currency_digital_code, currency_letter_code,
                      currency_name)
VALUES (1, '643', 'RUB', 'Russian Ruble'),
       (2, '980', 'UAH', 'Hryvnia'),
       (3, '840', 'USD', 'US Dollar'),
       (4, '978', 'EUR', 'Euro'),
       (5, '392', 'JPY', 'Yen'),
       (6, '156', 'CNY', 'Yuan Renminbi'),
       (7, '826', 'GBP', 'Pound Sterling');
SELECT setval('currency_id_seq', (SELECT MAX(id) FROM currency));

INSERT INTO issuing_bank (id, bic, bin, abbreviated_name)
VALUES (1, '041234569', '12345', 'ПАО Банк-эмитент №1'),
       (2, '041234570', '12346', 'ПАО Банк-эмитент №2'),
       (3, '041234571', '12347', 'ПАО Банк-эмитент №3');
SELECT setval('issuing_bank_id_seq', (SELECT MAX(id) FROM issuing_bank));

INSERT INTO acquiring_bank (id, bic, abbreviated_name)
VALUES (1, '041234567', 'ПАО Банк-эквайер №1'),
       (2, '041234568', 'ПАО Банк-эквайер №2'),
       (3, '041234569', 'ПАО Банк-эквайер №3');
SELECT setval('acquiring_bank_id_seq', (SELECT MAX(id) FROM acquiring_bank));

INSERT INTO sales_point (id, pos_name, pos_address, pos_inn,
                         acquiring_bank_id)
VALUES (1, 'Shop №1', 'City, 1-st 1', '1234567890', 1),
       (2, 'Shop №2', 'City, 2-st 2', '1234567891', 2),
       (3, 'Shop №3', 'City, 3-st 3', '1234567892', 1);
SELECT setval('sales_point_id_seq', (SELECT MAX(id) FROM sales_point));

INSERT INTO merchant_category_code (id, mcc, mcc_name)
VALUES (1, '5309', 'Беспошлинные магазины Duty Free'),
       (2, '5651', 'Одежда для всей семьи'),
       (3, '5691', 'Магазины мужской и женской одежды'),
       (4, '5812', 'Места общественного питания, рестораны'),
       (5, '5814', 'Фастфуд');
SELECT setval('merchant_category_code_id_seq', (SELECT MAX(id) FROM merchant_category_code));

INSERT INTO terminal (id, terminal_id, mcc_id, pos_id)
VALUES (1, '000000001', 1, 1),
       (2, '000000002', 2, 2),
       (3, '000000003', 3, 3);
SELECT setval('terminal_id_seq', (SELECT MAX(id) FROM terminal));

INSERT INTO response_code (id, error_code, error_description, error_level)
VALUES (1, '00', 'одобрено и завершено', 'Всё в порядке'),
       (2, '01', 'авторизация отклонена, обратиться в банк-эмитент', 'не критическая'),
       (3, '03', 'незарегестрированная торговая точка или агрегатор платежей', 'не критическая'),
       (4, '05', 'авторизация отклонена, оплату не проводить', 'критическая'),
       (5, '41', 'карта утеряна, изъять', 'критическая'),
       (6, '51', 'недостаточно средств на счёте', 'сервисная или аппаратная ошибка'),
       (7, '55', 'неправильный PIN', 'не критическая');
SELECT setval('response_code_id_seq', (SELECT MAX(id) FROM response_code));

INSERT INTO transaction_type (id, transaction_type_name, operator)
VALUES (1, 'Списание со счёта', '-'),
       (2, 'Пополнение счёта', '+');
SELECT setval('transaction_type_id_seq', (SELECT MAX(id) FROM transaction_type));

INSERT INTO account (id, account_number, balance, currency_id, issuing_bank_id)
VALUES (1, '40817810800000000001', 649.7, 1, 1),
       (2, '40817810100000000002', 48702.07, 1, 1),
       (3, '40817810400000000003', 715000.01, 1, 1),
       (4, '40817840000000000004', 10000.0, 3, 1);
SELECT setval('account_id_seq', (SELECT MAX(id) FROM account));

INSERT INTO card (id, card_number, expiration_date, holder_name, card_status_id,
                  payment_system_id, account_id, received_from_issuing_bank,
                  sent_to_issuing_bank)
VALUES (1, '4123450000000019', '2025-12-31', 'IVAN I.IVANOV', 2, 1, 1, '2022-10-21 15:26:06.175',
        '2022-10-21 15:27:08.271'),
       (2, '5123450000000024', '2025-12-31', 'SEMION E.PETROV', 3, 2, 2, '2022-04-05 10:23:05.372',
        '2022-04-05 10:24:02.175');
SELECT setval('card_id_seq', (SELECT MAX(id) FROM card));

INSERT INTO transaction (id, transaction_date, sum, transaction_name, account_id, transaction_type_id,
                         received_from_issuing_bank)
VALUES (1, '2022-10-22', 10.11, 'Cash deposit', 1, 2, '2022-10-22 09:05:23.129'),
       (2, '2022-04-06', 50.92, 'Cash deposit', 1, 2, '2022-04-06 12:03:41.861');
SELECT setval('transaction_id_seq', (SELECT MAX(id) FROM transaction));
