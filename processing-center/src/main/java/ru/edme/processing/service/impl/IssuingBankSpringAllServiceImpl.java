package ru.edme.processing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.processing.dto.IssuingBankDto;
import ru.edme.processing.mapper.IssuingBankMapper;
import ru.edme.processing.model.IssuingBank;
import ru.edme.processing.repository.IssuingBankRepository;
import ru.edme.processing.service.IssuingBankAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IssuingBankSpringAllServiceImpl implements IssuingBankAllService {

    private final IssuingBankRepository issuingBankRepository;
    private final IssuingBankMapper issuingBankMapper;
    private final Class<IssuingBank> entityClass = IssuingBank.class;

    @Override
    @Transactional
    public IssuingBankDto save(IssuingBankDto entity) {
        IssuingBank issuingBank = issuingBankMapper.toIssuingBank(entity);
        IssuingBank saved = issuingBankRepository.save(issuingBank);

        return issuingBankMapper.toIssuingBankDto(saved);
    }

    @Override
    public IssuingBankDto findById(Long id) {
        return issuingBankRepository.findById(id)
                .map(issuingBankMapper::toIssuingBankDto)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
                );
    }

    @Override
    public List<IssuingBankDto> findAll() {
        return issuingBankRepository.findAll().stream()
                .map(issuingBankMapper::toIssuingBankDto)
                .toList();
    }

    @Override
    @Transactional
    public IssuingBankDto update(IssuingBankDto entity) {
        IssuingBankDto issuingBankDto = findById(entity.getId());
        issuingBankDto.setBic(entity.getBic());
        issuingBankDto.setBin(entity.getBin());
        issuingBankDto.setAbbreviatedName(entity.getAbbreviatedName());
        IssuingBank issuingBank = issuingBankMapper.toIssuingBank(issuingBankDto);
        IssuingBank saved = issuingBankRepository.save(issuingBank);

        return issuingBankMapper.toIssuingBankDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        IssuingBankDto issuingBankDto = findById(id);
        IssuingBank issuingBank = issuingBankMapper.toIssuingBank(issuingBankDto);
        issuingBankRepository.delete(issuingBank);

        return true;
    }
}
