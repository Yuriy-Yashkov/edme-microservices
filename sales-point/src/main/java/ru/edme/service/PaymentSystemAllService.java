package ru.edme.service;

import ru.edme.dto.requestDTO.PaymentSystemRequestDTO;
import ru.edme.dto.responseDTO.PaymentSystemResponseDTO;
import ru.edme.model.PaymentSystem;

public interface PaymentSystemAllService extends AllService<Long, PaymentSystem, PaymentSystemRequestDTO, PaymentSystemResponseDTO> {

}
