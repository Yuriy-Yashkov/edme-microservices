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
import ru.edme.issuing.dto.TransactionTypeDto;
import ru.edme.issuing.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/issuing-bank/transaction-types")
@Tag(name = "TransactionType Controller", description = "Управление типами (TransactionType)")
public class TransactionTypeController {

    private final AllService<TransactionTypeDto, Long> transactionTypeAllServiceImpl;

    @Operation(summary = "Создать новый тип транзакции", description = "Создаёт новый тип транзакции и возвращает его данные")
    @ApiResponse(responseCode = "201", description = "Тип транзакции успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransactionTypeDto.class)))
    @PostMapping
    public ResponseEntity<TransactionTypeDto> create(@Valid @RequestBody TransactionTypeDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionTypeAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить тип транзакции по ID", description = "Возвращает тип транзакции по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Тип транзакции найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransactionTypeDto.class)))
    @ApiResponse(responseCode = "404", description = "Тип транзакции не найден")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionTypeDto> findById(@Parameter(description = "ID типа транзакции", example = "1")
                                                       @PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionTypeAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех типов транзакции", description = "Возвращает все типы транзакции из базы данных")
    @ApiResponse(responseCode = "200", description = "Список типов транзакции",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransactionTypeDto.class)))
    @GetMapping
    public ResponseEntity<List<TransactionTypeDto>> findAll() {
        return ResponseEntity.ok(transactionTypeAllServiceImpl.findAll());
    }

    @Operation(summary = "Обновить данные типа транзакции", description = "Обновляет существующий тип транзакции")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TransactionTypeDto.class)))
    @PutMapping
    public ResponseEntity<TransactionTypeDto> update(@Valid @RequestBody TransactionTypeDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(transactionTypeAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить тип транзакции", description = "Удаляет тип транзакции по его ID")
    @ApiResponse(responseCode = "200", description = "Тип транзакции успешно удалён")
    @ApiResponse(responseCode = "404", description = "Тип транзакции не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@Parameter(description = "ID типа транзакции", example = "1")
                                          @PathVariable("id") Long id) {
        boolean delete = transactionTypeAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
}
