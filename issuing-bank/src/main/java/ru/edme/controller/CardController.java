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
import ru.edme.dto.CardDto;
import ru.edme.model.Account;
import ru.edme.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/issuing-bank/accounts/cards")
@Tag(name = "Card Controller", description = "Управление картой")
public class CardController {

    private final AllService<CardDto, Long> cardAllServiceImpl;

    @Operation(summary = "Создать новую карту", description = "Создаёт новую карту и возвращает её данные")
    @ApiResponse(responseCode = "201", description = "Карта успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PostMapping
    public ResponseEntity<CardDto> create(@Valid @RequestBody CardDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить карту по ID", description = "Возвращает карту по её уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Карта найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @ApiResponse(responseCode = "404", description = "Карта не найдена")
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> findById(@Parameter(description = "ID карты", example = "1")
                                            @PathVariable("id") Long id) {
        return ResponseEntity.ok(cardAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех карт", description = "Возвращает все карты из базы данных")
    @ApiResponse(responseCode = "200", description = "Список карт",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @GetMapping
    public ResponseEntity<List<CardDto>> findAll() {
        return ResponseEntity.ok(cardAllServiceImpl.findAll());
    }

    @Operation(summary = "Обновить данные карты", description = "Обновляет существующую карту")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PutMapping
    public ResponseEntity<CardDto> update(@Valid @RequestBody CardDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(cardAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить карту", description = "Удаляет карту по его ID")
    @ApiResponse(responseCode = "200", description = "Карта успешно удалена")
    @ApiResponse(responseCode = "404", description = "Карта не найдена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID карты", example = "1")
                                       @PathVariable("id") Long id) {
        boolean delete = cardAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
