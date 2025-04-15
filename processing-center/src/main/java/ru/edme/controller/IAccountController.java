package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.edme.model.Account;

import java.util.List;

@Tag(name = "Account Controller", description = "Управление счетами (Account)")
public interface IAccountController {

    @Operation(summary = "Создать новый аккаунт", description = "Создаёт новый аккаунт и возвращает его данные")
    @ApiResponse(responseCode = "201", description = "Аккаунт успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PostMapping
    ResponseEntity<Account> create(@RequestBody Account entity);

    @Operation(summary = "Получить аккаунт по ID", description = "Возвращает аккаунт по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Аккаунт найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @ApiResponse(responseCode = "404", description = "Аккаунт не найден")
    @GetMapping("/{id}")
    ResponseEntity<Account> findById(@Parameter(description = "ID аккаунта", example = "1")
                                     @PathVariable("id") Long id);

    @Operation(summary = "Получить список всех аккаунтов", description = "Возвращает все аккаунты из базы данных")
    @ApiResponse(responseCode = "200", description = "Список аккаунтов",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @GetMapping
    ResponseEntity<List<Account>> findAll();

    @Operation(summary = "Обновить данные аккаунта", description = "Обновляет существующий аккаунт")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PutMapping
    ResponseEntity<Account> update(@RequestBody Account entity);

    @Operation(summary = "Удалить аккаунт", description = "Удаляет аккаунт по его ID")
    @ApiResponse(responseCode = "200", description = "Аккаунт успешно удалён")
    @ApiResponse(responseCode = "404", description = "Аккаунт не найден")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@Parameter(description = "ID аккаунта", example = "1") @PathVariable("id") Long id);
}
