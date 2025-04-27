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
import ru.edme.dto.CardStatusDto;
import ru.edme.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/issuing-bank/card-statuses")
@Tag(name = "CardStatus Controller", description = "Управление статусами карты")
public class CardStatusController {

    private final AllService<CardStatusDto, Long> cardStatusAllServiceImpl;

    @Operation(summary = "Создать новый статус карты", description = "Создаёт новый статус карты и возвращает его данные")
    @ApiResponse(responseCode = "201", description = "Статус карты успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardStatusDto.class)))
    @PostMapping
    public ResponseEntity<CardStatusDto> create(@Valid @RequestBody CardStatusDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardStatusAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить статус карты по ID", description = "Возвращает статус карты по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Статус карты найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardStatusDto.class)))
    @ApiResponse(responseCode = "404", description = "Статус карты не найден")
    @GetMapping("/{id}")
    public ResponseEntity<CardStatusDto> findById(@Parameter(description = "ID аккаунта", example = "1")
                                                  @PathVariable("id") Long id) {
        return ResponseEntity.ok(cardStatusAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех статусов карты", description = "Возвращает все статусы карты из базы данных")
    @ApiResponse(responseCode = "200", description = "Список аккаунтов",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardStatusDto.class)))
    @GetMapping
    public ResponseEntity<List<CardStatusDto>> findAll() {
        return ResponseEntity.ok(cardStatusAllServiceImpl.findAll());
    }

    @Operation(summary = "Обновить данные статуса карты", description = "Обновляет существующий статус карты")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardStatusDto.class)))
    @PutMapping
    public ResponseEntity<CardStatusDto> update(@Valid @RequestBody CardStatusDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(cardStatusAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить статус карты", description = "Удаляет статус карты по его ID")
    @ApiResponse(responseCode = "200", description = "Статус карты успешно удалён")
    @ApiResponse(responseCode = "404", description = "Статус карты не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@Parameter(description = "ID статуса карты", example = "1")
                                          @PathVariable("id") Long id) {
        boolean delete = cardStatusAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
}
