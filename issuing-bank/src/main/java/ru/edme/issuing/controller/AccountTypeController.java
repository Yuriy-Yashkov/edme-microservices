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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edme.issuing.dto.AccountTypeDto;
import ru.edme.issuing.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/issuing-bank/account-types")
@Tag(name = "AccountType Controller", description = "Управление типами (AccountType)")
public class AccountTypeController {

    private final AllService<AccountTypeDto, Long> accountAllServiceImpl;

    @Operation(summary = "Создать новый тип аккаунта", description = "Создаёт новый тип аккаунта и возвращает его данные")
    @ApiResponse(responseCode = "201", description = "Тип аккаунта успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountTypeDto.class)))
    @PostMapping
    public ResponseEntity<AccountTypeDto> create(@Valid @RequestBody AccountTypeDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить тип аккаунта по ID", description = "Возвращает тип аккаунта по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Аккаунт найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountTypeDto.class)))
    @ApiResponse(responseCode = "404", description = "Тип аккаунта не найден")
    @GetMapping("/{id}")
    public ResponseEntity<AccountTypeDto> findById(@Parameter(description = "ID типа аккаунта", example = "1")
                                                   @PathVariable("id") Long id) {
        return ResponseEntity.ok(accountAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех типов аккаунта", description = "Возвращает все типы аккаунта из базы данных")
    @ApiResponse(responseCode = "200", description = "Список типов аккаунта",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountTypeDto.class)))
    @GetMapping
    public ResponseEntity<List<AccountTypeDto>> findAll() {
        return ResponseEntity.ok(accountAllServiceImpl.findAll());
    }

    @Operation(summary = "Обновить данные типа аккаунта", description = "Обновляет существующий тип аккаунта")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountTypeDto.class)))
    @PutMapping
    public ResponseEntity<AccountTypeDto> update(@Valid @RequestBody AccountTypeDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(accountAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить аккаунт", description = "Удаляет тип аккаунта по его ID")
    @ApiResponse(responseCode = "200", description = "Тип аккаунта успешно удалён")
    @ApiResponse(responseCode = "404", description = "Тип аккаунта не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@Parameter(description = "ID типа аккаунта", example = "1")
                                          @PathVariable("id") Long id) {
        boolean delete = accountAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
}
