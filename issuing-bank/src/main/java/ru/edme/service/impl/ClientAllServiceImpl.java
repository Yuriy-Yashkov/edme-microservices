package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.dto.ClientDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.ClientMapper;
import ru.edme.model.Client;
import ru.edme.repository.ClientRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientAllServiceImpl implements AllService<ClientDto, Long> {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final Class<Client> entityClass = Client.class;

    @Override
    public ClientDto save(ClientDto entity) {
        Client saved = clientRepository.save(clientMapper.toClient(entity));

        return clientMapper.toClientDto(saved);
    }

    @Override
    public ClientDto findById(Long id) {
        return clientRepository.findById(id)
                .map(clientMapper::toClientDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toClientDto)
                .toList();
    }

    @Override
    public ClientDto update(ClientDto entity) {
        ClientDto clientDto = findById(entity.getId());
        clientDto.setLastName(entity.getLastName() != null ? entity.getLastName() : clientDto.getLastName());
        clientDto.setFirstName(entity.getFirstName() != null ? entity.getFirstName() : clientDto.getFirstName());
        clientDto.setMiddleName(entity.getMiddleName() != null ? entity.getMiddleName() : clientDto.getMiddleName());
        clientDto.setBirthDate(entity.getBirthDate() != null ? entity.getBirthDate() : clientDto.getBirthDate());
        clientDto.setDocument(entity.getDocument() != null ? entity.getDocument() : clientDto.getDocument());
        clientDto.setAddress(entity.getAddress() != null ? entity.getAddress() : clientDto.getAddress());
        clientDto.setPhone(entity.getPhone() != null ? entity.getPhone() : clientDto.getPhone());
        clientDto.setEmail(entity.getEmail() != null ? entity.getEmail() : clientDto.getEmail());

        return save(clientDto);
    }

    @Override
    public boolean delete(Long id) {
        ClientDto clientDto = findById(id);
        clientRepository.delete(clientMapper.toClient(clientDto));

        return true;
    }
}
