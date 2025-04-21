package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.dto.PaymentSystemDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.PaymentSystemMapper;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.PaymentSystemRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentSystemAllServiceImpl implements AllService<PaymentSystemDto, Long> {

    private final PaymentSystemRepository paymentSystemRepository;
    private final PaymentSystemMapper paymentSystemMapper;
    private final Class<PaymentSystem> entityClass = PaymentSystem.class;

    @Override
    public PaymentSystemDto save(PaymentSystemDto entity) {
        PaymentSystem saved = paymentSystemRepository.save(paymentSystemMapper.toPaymentSystem(entity));

        return paymentSystemMapper.toPaymentSystemDto(saved);
    }

    @Override
    public PaymentSystemDto findById(Long id) {
        return paymentSystemRepository.findById(id)
                .map(paymentSystemMapper::toPaymentSystemDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<PaymentSystemDto> findAll() {
        return paymentSystemRepository.findAll().stream()
                .map(paymentSystemMapper::toPaymentSystemDto)
                .toList();
    }

    @Override
    public PaymentSystemDto update(PaymentSystemDto entity) {
        PaymentSystemDto paymentSystemDto = findById(entity.getId());
        paymentSystemDto.setPaymentSystemName(entity.getPaymentSystemName());
        paymentSystemDto.setFirstDigitBin(entity.getFirstDigitBin());

        return save(paymentSystemDto);
    }

    @Override
    public boolean delete(Long id) {
        PaymentSystemDto paymentSystemDto = findById(id);
        paymentSystemRepository.delete(paymentSystemMapper.toPaymentSystem(paymentSystemDto));

        return true;
    }
}
