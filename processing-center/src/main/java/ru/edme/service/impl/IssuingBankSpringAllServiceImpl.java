package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.IssuingBank;
import ru.edme.repository.IssuingBankRepository;
import ru.edme.service.IssuingBankAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IssuingBankSpringAllServiceImpl implements IssuingBankAllService {

    private final IssuingBankRepository issuingBankRepository;
    private final Class<IssuingBank> entityClass = IssuingBank.class;

    @Override
    @Transactional
    public IssuingBank save(IssuingBank entity) {
        return issuingBankRepository.save(entity);
    }

    @Override
    public IssuingBank findById(Long id) {
        return issuingBankRepository.findById(id).orElseThrow(
                () -> new RuntimeException(
                        String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
        );
    }

    @Override
    public List<IssuingBank> findAll() {
        return issuingBankRepository.findAll();
    }

    @Override
    @Transactional
    public IssuingBank update(IssuingBank entity) {
        IssuingBank issuingBank = findById(entity.getId());
        issuingBank.setBic(entity.getBic());
        issuingBank.setBin(entity.getBin());
        issuingBank.setAbbreviatedName(entity.getAbbreviatedName());

        return issuingBankRepository.save(issuingBank);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        IssuingBank issuingBank = findById(id);
        issuingBankRepository.delete(issuingBank);
        return true;
    }
}
