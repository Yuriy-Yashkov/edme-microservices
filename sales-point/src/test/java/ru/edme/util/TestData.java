package ru.edme.util;

import ru.edme.dto.requestDTO.CardRequestDTO;
import ru.edme.dto.requestDTO.PaymentSystemRequestDTO;
import ru.edme.dto.responseDTO.CardResponseDTO;
import ru.edme.dto.responseDTO.PaymentSystemResponseDTO;
import ru.edme.model.Card;
import ru.edme.model.PaymentSystem;

import java.time.LocalDate;

public class TestData {

    public PaymentSystem paymentSystem = PaymentSystem.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemRequestDTO paymentSystemRequestDTO = PaymentSystemRequestDTO.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemRequestDTO paymentSystemRequestDTOId = PaymentSystemRequestDTO.builder()
            .id(3L)
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemResponseDTO paymentSystemResponseDTOId = PaymentSystemResponseDTO.builder()
            .id(3L)
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystemResponseDTO paymentSystemResponseDTO = PaymentSystemResponseDTO.builder()
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
    public CardRequestDTO cardRequestDTO = CardRequestDTO.builder()
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystemRequestDTO(paymentSystemRequestDTO)
            .build();
    public CardRequestDTO cardRequestDTOId = CardRequestDTO.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystemRequestDTO(paymentSystemRequestDTOId)
            .build();
    public CardResponseDTO cardResponseDTOId = CardResponseDTO.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystemResponseDTO(paymentSystemResponseDTO)
            .build();
}
