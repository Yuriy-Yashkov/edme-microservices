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
import ru.edme.dto.CurrencyDto;
import ru.edme.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/issuing-bank/currency")
@Tag(name = "Account Controller", description = "Управление валютой (AccountType)")
public class CurrencyController {

    private final AllService<CurrencyDto, Long> currencyAllServiceImpl;

    @Operation(summary = "Создать новую валюту", description = "Создаёт новый валюту и возвращает её данные")
    @ApiResponse(responseCode = "201", description = "Валюта успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CurrencyDto.class)))
    @PostMapping
    public ResponseEntity<CurrencyDto> create(@Valid @RequestBody CurrencyDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(currencyAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить валюту по ID", description = "Возвращает валюту по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Аккаунт найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CurrencyDto.class)))
    @ApiResponse(responseCode = "404", description = "Валюта не найден")
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> findById(@Parameter(description = "ID валюты", example = "1")
                                                @PathVariable("id") Long id) {
        return ResponseEntity.ok(currencyAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех валют", description = "Возвращает все валюты из базы данных")
    @ApiResponse(responseCode = "200", description = "Список валют",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CurrencyDto.class)))
    @GetMapping
    public ResponseEntity<List<CurrencyDto>> findAll() {
        return ResponseEntity.ok(currencyAllServiceImpl.findAll());
    }

    @Operation(summary = "Обновить данные валюты", description = "Обновляет существующие валюты")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CurrencyDto.class)))
    @PutMapping
    public ResponseEntity<CurrencyDto> update(@Valid @RequestBody CurrencyDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(currencyAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить валюту", description = "Удаляет валюту по его ID")
    @ApiResponse(responseCode = "200", description = "Валюта успешно удалён")
    @ApiResponse(responseCode = "404", description = "Валюта не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@Parameter(description = "ID валюты", example = "1")
                                          @PathVariable("id") Long id) {
        boolean delete = currencyAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
}
