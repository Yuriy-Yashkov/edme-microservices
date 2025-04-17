package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.PaymentSystemDto;
import ru.edme.mapper.PaymentSystemMapper;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.PaymentSystemRepository;
import ru.edme.service.PaymentSystemAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentSystemSpringAllServiceImpl implements PaymentSystemAllService {

    private final PaymentSystemRepository paymentSystemRepository;
    private final PaymentSystemMapper paymentSystemMapper;
    private final Class<PaymentSystem> entityClass = PaymentSystem.class;

    @Override
    @Transactional
    public PaymentSystemDto save(PaymentSystemDto entity) {
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(entity);
        PaymentSystem saved = paymentSystemRepository.save(paymentSystem);

        return paymentSystemMapper.toPaymentSystemDto(saved);
    }

    @Override
    public PaymentSystemDto findById(Long id) {
        return paymentSystemRepository.findById(id)
                .map(paymentSystemMapper::toPaymentSystemDto)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
                );
    }

    @Override
    public List<PaymentSystemDto> findAll() {
        return paymentSystemRepository.findAll().stream()
                .map(paymentSystemMapper::toPaymentSystemDto)
                .toList();
    }

    @Override
    @Transactional
    public PaymentSystemDto update(PaymentSystemDto entity) {
        PaymentSystemDto paymentSystemDto = findById(entity.getId());
        paymentSystemDto.setPaymentSystemName(entity.getPaymentSystemName());
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(paymentSystemDto);
        PaymentSystem saved = paymentSystemRepository.save(paymentSystem);

        return paymentSystemMapper.toPaymentSystemDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        PaymentSystemDto paymentSystemDto = findById(id);
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(paymentSystemDto);
        paymentSystemRepository.delete(paymentSystem);

        return true;
    }
}
