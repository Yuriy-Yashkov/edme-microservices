package ru.edme.processing.util;

import ru.edme.processing.dto.AccountDto;
import ru.edme.processing.dto.CardDto;
import ru.edme.processing.dto.CardStatusDto;
import ru.edme.processing.dto.CurrencyDto;
import ru.edme.processing.dto.IssuingBankDto;
import ru.edme.processing.dto.PaymentSystemDto;
import ru.edme.processing.model.Account;
import ru.edme.processing.model.Card;
import ru.edme.processing.model.CardStatus;
import ru.edme.processing.model.Currency;
import ru.edme.processing.model.IssuingBank;
import ru.edme.processing.model.PaymentSystem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestData {

    public CardStatusDto cardStatusDto = CardStatusDto.builder()
            .cardStatusName("Какой-то статус")
            .build();
    public CardStatusDto cardStatusDtoId = CardStatusDto.builder()
            .id(3L)
            .cardStatusName("Какой-то статус")
            .build();
    public CardStatus cardStatus = CardStatus.builder()
            .cardStatusName("Какой-то статус")
            .build();
    public PaymentSystemDto paymentSystemDto = PaymentSystemDto.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemDto paymentSystemDtoId = PaymentSystemDto.builder()
            .id(3L)
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystem paymentSystem = PaymentSystem.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public IssuingBankDto issuingBankDto = IssuingBankDto.builder()
            .bic("123456789")
            .bin("12345")
            .abbreviatedName("Какая-то аббревиатура")
            .build();
    public IssuingBankDto issuingBankDtoId = IssuingBankDto.builder()
            .id(3L)
            .bic("123456789")
            .bin("12345")
            .abbreviatedName("Какая-то аббревиатура")
            .build();
    public IssuingBank issuingBank = IssuingBank.builder()
            .bic("123456789")
            .bin("12345")
            .abbreviatedName("Какая-то аббревиатура")
            .build();
    public CurrencyDto currencyDto = CurrencyDto.builder()
            .currencyDigitalCode("123")
            .currencyLetterCode("000")
            .currencyName("Какая-то валюта")
            .build();
    public CurrencyDto currencyDtoId = CurrencyDto.builder()
            .id(3L)
            .currencyDigitalCode("123")
            .currencyLetterCode("000")
            .currencyName("Какая-то валюта")
            .build();
    public Currency currency = Currency.builder()
            .currencyDigitalCode("123")
            .currencyLetterCode("000")
            .currencyName("Какая-то валюта")
            .build();
    public AccountDto accountDto = AccountDto.builder()
            .accountNumber("Какой-то номер")
            .balance(new BigDecimal("50.0"))
            .currency(currencyDto)
            .issuingBank(issuingBankDto)
            .build();
    public AccountDto accountDtoId = AccountDto.builder()
            .id(3L)
            .accountNumber("Какой-то номер")
            .balance(new BigDecimal("50.0"))
            .currency(currencyDtoId)
            .issuingBank(issuingBankDtoId)
            .build();
    public Account account = Account.builder()
            .accountNumber("Какой-то номер")
            .balance(new BigDecimal("50.0"))
            .currency(currency)
            .issuingBank(issuingBank)
            .build();
    public CardDto cardDto = CardDto.builder()
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .cardStatus(cardStatusDto)
            .paymentSystem(paymentSystemDto)
            .account(accountDto)
            .receivedFromIssuingBank(LocalDateTime.of(2024, 3, 13, 14, 30, 15))
            .sentToIssuingBank(LocalDateTime.of(2024, 3, 13, 14, 30, 15))
            .build();
    public CardDto cardDtoId = CardDto.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .cardStatus(cardStatusDtoId)
            .paymentSystem(paymentSystemDtoId)
            .account(accountDtoId)
            .receivedFromIssuingBank(LocalDateTime.of(2024, 3, 13, 14, 30, 15))
            .sentToIssuingBank(LocalDateTime.of(2024, 3, 13, 14, 30, 15))
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
}
