package ru.edme.sales.service;

import ru.edme.sales.dto.requestDto.PaymentSystemRequestDto;
import ru.edme.sales.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.sales.util.ListWrapper;

public interface PaymentSystemService {

    PaymentSystemResponseDto save(PaymentSystemRequestDto entity);

    PaymentSystemResponseDto findById(Long id);

    ListWrapper<PaymentSystemResponseDto> findAllWrapped();

    PaymentSystemResponseDto update(PaymentSystemRequestDto entity);

    boolean delete(Long id);
}
