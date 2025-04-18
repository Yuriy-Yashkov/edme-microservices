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
import ru.edme.dto.IssuingBankDto;
import ru.edme.model.IssuingBank;
import ru.edme.service.IssuingBankAllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cards/accounts/issuing-banks")
@Tag(name = "Issuing Bank Controller", description = "Управление банками-эмитентами")
public class IssuingBankController {

    private final IssuingBankAllService issuingBankAllService;

    @Operation(summary = "Создать банк-эмитент", description = "Добавляет новый банк-эмитент и возвращает его")
    @ApiResponse(responseCode = "201", description = "Банк-эмитент успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IssuingBank.class)))
    @PostMapping
    public ResponseEntity<IssuingBankDto> create(@Valid @RequestBody IssuingBankDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(issuingBankAllService.save(entity));
    }

    @Operation(summary = "Получить банк-эмитент по ID", description = "Возвращает банк-эмитент по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Банк-эмитент найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IssuingBank.class)))
    @ApiResponse(responseCode = "404", description = "Банк-эмитент не найден")
    @GetMapping("/{id}")
    public ResponseEntity<IssuingBankDto> findById(@Parameter(description = "ID банка-эмитента", example = "1")
                                                   @PathVariable("id") Long id) {
        return ResponseEntity.ok(issuingBankAllService.findById(id));
    }

    @Operation(summary = "Получить список всех банков-эмитентов", description = "Возвращает список всех банков-эмитентов")
    @ApiResponse(responseCode = "200", description = "Список банков-эмитентов",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IssuingBank.class)))
    @GetMapping
    public ResponseEntity<List<IssuingBankDto>> findAll() {
        return ResponseEntity.ok(issuingBankAllService.findAll());
    }

    @Operation(summary = "Обновить банк-эмитент", description = "Обновляет существующий банк-эмитент")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IssuingBank.class)))
    @PutMapping
    public ResponseEntity<IssuingBankDto> update(@Valid @RequestBody IssuingBankDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(issuingBankAllService.update(entity));
    }

    @Operation(summary = "Удалить банк-эмитент", description = "Удаляет банк-эмитент по его ID")
    @ApiResponse(responseCode = "200", description = "Банк-эмитент успешно удалён")
    @ApiResponse(responseCode = "404", description = "Банк-эмитент не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID банка-эмитента", example = "1") @PathVariable("id") Long id) {
        boolean delete = issuingBankAllService.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
