package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.ClientDto;
import ru.edme.model.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toClientDto(Client client);

    Client toClient(ClientDto clientDto);
}
