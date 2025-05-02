package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.ClientDto;
import ru.edme.issuing.model.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toClientDto(Client client);

    Client toClient(ClientDto clientDto);
}
