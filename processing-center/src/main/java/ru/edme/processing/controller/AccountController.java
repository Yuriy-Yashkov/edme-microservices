package ru.edme.processing.controller;

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
import ru.edme.processing.dto.AccountDto;
import ru.edme.processing.model.Account;
import ru.edme.processing.service.AccountAllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("v1/processing-center/accounts")
@Tag(name = "Account Controller", description = "Управление счетами (Account)")
public class AccountController {

    private final AccountAllService accountAllService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Создать новый аккаунт", description = "Создаёт новый аккаунт и возвращает его данные")
    @ApiResponse(responseCode = "201", description = "Аккаунт успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PostMapping
    public ResponseEntity<AccountDto> create(@Valid @RequestBody AccountDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountAllService.save(entity));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "Получить аккаунт по ID", description = "Возвращает аккаунт по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Аккаунт найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @ApiResponse(responseCode = "404", description = "Аккаунт не найден")
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> findById(@Parameter(description = "ID аккаунта", example = "1")
                                               @PathVariable("id") Long id) {
        return ResponseEntity.ok(accountAllService.findById(id));
    }

    @Operation(summary = "Получить список всех аккаунтов", description = "Возвращает все аккаунты из базы данных")
    @ApiResponse(responseCode = "200", description = "Список аккаунтов",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @GetMapping
    public ResponseEntity<List<AccountDto>> findAll() {
        return ResponseEntity.ok(accountAllService.findAll());
    }

    @Operation(summary = "Обновить данные аккаунта", description = "Обновляет существующий аккаунт")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PutMapping
    public ResponseEntity<AccountDto> update(@Valid @RequestBody AccountDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(accountAllService.update(entity));
    }

    @Operation(summary = "Удалить аккаунт", description = "Удаляет аккаунт по его ID")
    @ApiResponse(responseCode = "200", description = "Аккаунт успешно удалён")
    @ApiResponse(responseCode = "404", description = "Аккаунт не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID аккаунта", example = "1")
                                       @PathVariable("id") Long id) {
        boolean delete = accountAllService.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
