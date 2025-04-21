package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.dto.AccountTypeDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.AccountTypeMapper;
import ru.edme.model.AccountType;
import ru.edme.repository.AccountTypeRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTypeAllServiceImpl implements AllService<AccountTypeDto, Long> {

    private final AccountTypeRepository accountTypeRepository;
    private final AccountTypeMapper accountTypeMapper;
    private final Class<AccountType> entityClass = AccountType.class;

    @Override
    public AccountTypeDto save(AccountTypeDto entity) {
        AccountType saved = accountTypeRepository.save(accountTypeMapper.toAccountType(entity));

        return accountTypeMapper.toAccountTypeDto(saved);
    }

    @Override
    public AccountTypeDto findById(Long id) {
        return accountTypeRepository.findById(id)
                .map(accountTypeMapper::toAccountTypeDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<AccountTypeDto> findAll() {
        return accountTypeRepository.findAll().stream()
                .map(accountTypeMapper::toAccountTypeDto)
                .toList();
    }

    @Override
    public AccountTypeDto update(AccountTypeDto entity) {
        AccountTypeDto accountTypeDto = findById(entity.getId());
        accountTypeDto.setAccountTypeName(
                entity.getAccountTypeName() != null ? entity.getAccountTypeName() : accountTypeDto.getAccountTypeName());

        return save(accountTypeDto);
    }

    @Override
    public boolean delete(Long id) {
        AccountTypeDto accountTypeDto = findById(id);
        accountTypeRepository.delete(accountTypeMapper.toAccountType(accountTypeDto));

        return true;
    }
}
