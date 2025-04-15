package ru.edme.util;

import ru.edme.model.Account;
import ru.edme.model.Card;
import ru.edme.model.CardStatus;
import ru.edme.model.Currency;
import ru.edme.model.IssuingBank;
import ru.edme.model.PaymentSystem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestData {

    public CardStatus cardStatus = CardStatus.builder()
            .cardStatusName("Какой-то статус")
            .build();
    public CardStatus cardStatusId = CardStatus.builder()
            .id(3L)
            .cardStatusName("Какой-то статус")
            .build();
    public PaymentSystem paymentSystem = PaymentSystem.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystem paymentSystemId = PaymentSystem.builder()
            .id(3L)
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public IssuingBank issuingBank = IssuingBank.builder()
            .bic("123456789")
            .bin("12345")
            .abbreviatedName("Какая-то аббревиатура")
            .build();
    public IssuingBank issuingBankId = IssuingBank.builder()
            .id(3L)
            .bic("123456789")
            .bin("12345")
            .abbreviatedName("Какая-то аббревиатура")
            .build();
    public Currency currency = Currency.builder()
            .currencyDigitalCode("123")
            .currencyLetterCode("000")
            .currencyName("Какая-то валюта")
            .build();
    public Currency currencyId = Currency.builder()
            .id(3L)
            .currencyDigitalCode("123")
            .currencyLetterCode("000")
            .currencyName("Какая-то валюта")
            .build();
    public Account account = Account.builder()
            .accountNumber("Какой-то номер")
            .balance(new BigDecimal("50.0"))
            .currency(currency)
            .issuingBank(issuingBank)
            .build();
    public Account accountId = Account.builder()
            .id(3L)
            .accountNumber("Какой-то номер")
            .balance(new BigDecimal("50.0"))
            .currency(currencyId)
            .issuingBank(issuingBankId)
            .build();
    public Card card = Card.builder()
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .cardStatus(cardStatus)
            .paymentSystem(paymentSystem)
            .account(account)
            .receivedFromIssuingBank(LocalDateTime.of(2024, 3, 13, 14, 30, 15))
            .sentToIssuingBank(LocalDateTime.of(2024, 3, 13, 14, 30, 15))
            .build();
    public Card cardId = Card.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .cardStatus(cardStatusId)
            .paymentSystem(paymentSystemId)
            .account(accountId)
            .receivedFromIssuingBank(LocalDateTime.of(2024, 3, 13, 14, 30, 15))
            .sentToIssuingBank(LocalDateTime.of(2024, 3, 13, 14, 30, 15))
            .build();
}
