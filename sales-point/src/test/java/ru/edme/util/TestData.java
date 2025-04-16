package ru.edme.util;

import ru.edme.dto.requestDto.CardRequestDto;
import ru.edme.dto.requestDto.PaymentSystemRequestDto;
import ru.edme.dto.responseDto.CardResponseDto;
import ru.edme.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.model.Card;
import ru.edme.model.PaymentSystem;

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
            .paymentSystemRequestDTO(paymentSystemRequestDto)
            .build();
    public CardRequestDto cardRequestDtoId = CardRequestDto.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystemRequestDTO(paymentSystemRequestDtoId)
            .build();
    public CardResponseDto cardResponseDtoId = CardResponseDto.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystemResponseDTO(paymentSystemResponseDto)
            .build();
}
