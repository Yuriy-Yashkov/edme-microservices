package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edme.dto.CurrencyDto;
import ru.edme.model.Currency;
import ru.edme.service.CurrencyAllService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cards/accounts/currency")
@Tag(name = "Currency Controller", description = "Управление валютами")
public class CurrencyController {

    private final CurrencyAllService currencyAllService;

    @Operation(summary = "Создать новую валюту", description = "Создаёт новую валюту и возвращает её")
    @ApiResponse(responseCode = "201", description = "Валюта успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Currency.class)))
    @PostMapping
    public ResponseEntity<CurrencyDto> create(@RequestBody CurrencyDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(currencyAllService.save(entity));
    }

    @Operation(summary = "Получить валюту по ID", description = "Возвращает валюту по её уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Валюта найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Currency.class)))
    @ApiResponse(responseCode = "404", description = "Валюта не найдена")
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> findById(@Parameter(description = "ID валюты", example = "1")
                                                @PathVariable("id") Long id) {
        return ResponseEntity.ok(currencyAllService.findById(id));
    }

    @Operation(summary = "Получить список всех валют", description = "Возвращает список всех валют")
    @ApiResponse(responseCode = "200", description = "Список валют",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Currency.class)))
    @GetMapping
    public ResponseEntity<List<CurrencyDto>> findAll() {
        return ResponseEntity.ok(currencyAllService.findAll());
    }

    @Operation(summary = "Обновить валюту", description = "Обновляет существующую валюту")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Currency.class)))
    @PutMapping
    public ResponseEntity<CurrencyDto> update(@RequestBody CurrencyDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(currencyAllService.update(entity));
    }

    @Operation(summary = "Удалить валюту", description = "Удаляет валюту по её ID")
    @ApiResponse(responseCode = "200", description = "Валюта успешно удалена")
    @ApiResponse(responseCode = "404", description = "Валюта не найдена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID валюты", example = "1") @PathVariable("id") Long id) {
        boolean delete = currencyAllService.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
