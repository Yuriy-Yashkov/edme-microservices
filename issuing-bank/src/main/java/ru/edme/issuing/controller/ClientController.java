package ru.edme.issuing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edme.issuing.dto.ClientDto;
import ru.edme.issuing.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/v1/issuing-bank/clients")
@Tag(name = "Client Controller", description = "Управление (Client)")
public class ClientController {

    private final AllService<ClientDto, Long> clientAllServiceImpl;

    @Operation(summary = "Создать нового клиента", description = "Создаёт нового клиента и возвращает его данные")
    @ApiResponse(responseCode = "201", description = "Клиент успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientDto.class)))
    @PostMapping
    public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить клиента по ID", description = "Возвращает клиента по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Клиент найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientDto.class)))
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> findById(@Parameter(description = "ID клиента", example = "1")
                                              @PathVariable("id") Long id) {
        return ResponseEntity.ok(clientAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех клиентов", description = "Возвращает всех клиентов из базы данных")
    @ApiResponse(responseCode = "200", description = "Список клиентов",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientDto.class)))
    @GetMapping
    public ResponseEntity<List<ClientDto>> findAll() {
        return ResponseEntity.ok(clientAllServiceImpl.findAll());
    }

    @Operation(summary = "Обновить данные клиента", description = "Обновляет существующего клиента")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientDto.class)))
    @PutMapping
    public ResponseEntity<ClientDto> update(@Valid @RequestBody ClientDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(clientAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по его ID")
    @ApiResponse(responseCode = "200", description = "Клиент успешно удалён")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@Parameter(description = "ID клиента", example = "1")
                                          @PathVariable("id") Long id) {
        boolean delete = clientAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
}
