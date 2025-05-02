package ru.edme.issuing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.issuing.dto.BankSettingDto;
import ru.edme.issuing.exception.EntityNotFoundException;
import ru.edme.issuing.mapper.BankSettingMapper;
import ru.edme.issuing.model.BankSetting;
import ru.edme.issuing.repository.BankSettingRepository;
import ru.edme.issuing.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankSettingAllServiceImpl implements AllService<BankSettingDto, Long> {

    private final BankSettingRepository bankSettingRepository;
    private final BankSettingMapper bankSettingMapper;
    private final Class<BankSetting> entityClass = BankSetting.class;

    @Override
    public BankSettingDto save(BankSettingDto entity) {
        BankSetting saved = bankSettingRepository.save(bankSettingMapper.toBankSetting(entity));

        return bankSettingMapper.toBankSettingDto(saved);
    }

    @Override
    public BankSettingDto findById(Long id) {
        return bankSettingRepository.findById(id)
                .map(bankSettingMapper::toBankSettingDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<BankSettingDto> findAll() {
        return bankSettingRepository.findAll().stream()
                .map(bankSettingMapper::toBankSettingDto)
                .toList();
    }

    @Override
    public BankSettingDto update(BankSettingDto entity) {
        BankSettingDto bankSettingDto = findById(entity.getId());
        bankSettingDto.setSetting(entity.getSetting());
        bankSettingDto.setCurrentValue(entity.getCurrentValue());
        bankSettingDto.setDescription(entity.getDescription());

        return save(bankSettingDto);
    }

    @Override
    public boolean delete(Long id) {
        BankSettingDto bankSettingDto = findById(id);
        bankSettingRepository.delete(bankSettingMapper.toBankSetting(bankSettingDto));

        return true;
    }
}
