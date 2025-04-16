package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.requestDTO.PaymentSystemRequestDTO;
import ru.edme.dto.responseDTO.PaymentSystemResponseDTO;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.PaymentSystemMapper;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.PaymentSystemRepository;
import ru.edme.service.PaymentSystemAllService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentSystemSpringAllServiceImpl implements PaymentSystemAllService {

    private final PaymentSystemRepository paymentSystemRepository;
    private final Class<PaymentSystem> entityClass = PaymentSystem.class;
    private final PaymentSystemMapper paymentSystemMapper;

    @Override
    @Transactional
    @CachePut(value = "paymentSystem", key = "#result.id")
    public PaymentSystem save(PaymentSystemRequestDTO paymentSystemRequestDTO) {
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(paymentSystemRequestDTO);

        return paymentSystemRepository.save(paymentSystem);
    }

    @Override
    @Cacheable(value = "paymentSystem", key = "#id")
    public PaymentSystemResponseDTO findById(Long id) {
        log.info("Данные взяты из БД.");

        return paymentSystemRepository.findById(id)
                .map(paymentSystemMapper::toPaymentSystemResponseDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
                );
    }

    @Override
    @Cacheable(value = "paymentSystems", key = "'all'")
    public List<PaymentSystemResponseDTO> findAll() {
        log.info("Данные взяты из БД.");

        return paymentSystemRepository.findAll().stream()
                .map(paymentSystemMapper::toPaymentSystemResponseDto)
                .toList();
    }

    @Override
    @Transactional
    @CachePut(value = "paymentSystem", key = "#entity.id")
    public PaymentSystem update(PaymentSystemRequestDTO entity) {
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(findById(entity.getId()));

        paymentSystem.setPaymentSystemName(entity.getPaymentSystemName());

        return paymentSystemRepository.save(paymentSystem);
    }

    @Override
    @Transactional
    @CacheEvict(value = "paymentSystem", key = "#id")
    public boolean delete(Long id) {
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(findById(id));
        paymentSystemRepository.delete(paymentSystem);

        return true;
    }
}
