package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.PaymentSystemRepository;
import ru.edme.service.PaymentSystemAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentSystemSpringAllServiceImpl implements PaymentSystemAllService {

    private final PaymentSystemRepository paymentSystemRepository;
    private final Class<PaymentSystem> entityClass = PaymentSystem.class;

    @Override
    @Transactional
    public PaymentSystem save(PaymentSystem entity) {
        return paymentSystemRepository.save(entity);
    }

    @Override
    public PaymentSystem findById(Long id) {
        return paymentSystemRepository.findById(id).orElseThrow(
                () -> new RuntimeException(
                        String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
        );
    }

    @Override
    public List<PaymentSystem> findAll() {
        return paymentSystemRepository.findAll();
    }

    @Override
    @Transactional
    public PaymentSystem update(PaymentSystem entity) {
        PaymentSystem paymentSystem = findById(entity.getId());
        paymentSystem.setPaymentSystemName(entity.getPaymentSystemName());

        return paymentSystemRepository.save(paymentSystem);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        PaymentSystem paymentSystem = findById(id);
        paymentSystemRepository.delete(paymentSystem);

        return true;
    }
}
