package ru.edme.controller;

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
import ru.edme.dto.TransactionDto;
import ru.edme.model.Account;
import ru.edme.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/issuing-bank/transactions")
@Tag(name = "Account Controller", description = "Управление транзакциями")
public class TransactionController {

    private final AllService<TransactionDto, Long> transactionAllServiceImpl;

    @Operation(summary = "Создать новую транзакцию", description = "Создаёт новую транзакцию и возвращает её данные")
    @ApiResponse(responseCode = "201", description = "Транзакция успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PostMapping
    public ResponseEntity<TransactionDto> create(@Valid @RequestBody TransactionDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить транзакцию по ID", description = "Возвращает транзакцию по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Транзакция найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @ApiResponse(responseCode = "404", description = "Транзакция не найден")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> findById(@Parameter(description = "ID транзакции", example = "1")
                                                   @PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех транзакций", description = "Возвращает все транзакции из базы данных")
    @ApiResponse(responseCode = "200", description = "Список транзакций",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @GetMapping
    public ResponseEntity<List<TransactionDto>> findAll() {
        return ResponseEntity.ok(transactionAllServiceImpl.findAll());
    }

    // TODO: 22.04.2025 Возможно, оставить только чтение и создание !
    @Operation(summary = "Обновить данные транзакции", description = "Обновляет существующую транзакцию")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PutMapping
    public ResponseEntity<TransactionDto> update(@Valid @RequestBody TransactionDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(transactionAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить транзакцию", description = "Удаляет транзакцию по его ID")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно удалена")
    @ApiResponse(responseCode = "404", description = "Транзакция не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID аккаунта", example = "1")
                                       @PathVariable("id") Long id) {
        boolean delete = transactionAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
