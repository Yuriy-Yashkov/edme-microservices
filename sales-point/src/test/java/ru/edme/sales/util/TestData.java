package ru.edme.sales.util;

import ru.edme.sales.dto.requestDto.CardRequestDto;
import ru.edme.sales.dto.requestDto.PaymentSystemRequestDto;
import ru.edme.sales.dto.responseDto.CardResponseDto;
import ru.edme.sales.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.sales.model.Card;
import ru.edme.sales.model.PaymentSystem;

import java.time.LocalDate;

public class TestData {

    public PaymentSystem paymentSystem = PaymentSystem.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemRequestDto paymentSystemRequestDto = PaymentSystemRequestDto.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemRequestDto paymentSystemRequestDtoId = PaymentSystemRequestDto.builder()
            .id(3L)
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemResponseDto paymentSystemResponseDtoId = PaymentSystemResponseDto.builder()
            .id(3L)
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemResponseDto paymentSystemResponseDto = PaymentSystemResponseDto.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();

    public Card card = Card.builder()
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystem(paymentSystem)
            .build();
    public Card cardId = Card.builder()
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystem(paymentSystem)
            .build();
    public CardRequestDto cardRequestDto = CardRequestDto.builder()
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystem(paymentSystemRequestDto)
            .build();
    public CardRequestDto cardRequestDtoId = CardRequestDto.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystem(paymentSystemRequestDtoId)
            .build();
    public CardResponseDto cardResponseDtoId = CardResponseDto.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystem(paymentSystemResponseDto)
            .build();
}
