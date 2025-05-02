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
import ru.edme.issuing.dto.BankSettingDto;
import ru.edme.issuing.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/issuing-bank/bank-settings")
@Tag(name = "BankSetting Controller", description = "Управление настройками (BankSetting)")
public class BankSettingController {

    private final AllService<BankSettingDto, Long> bankSettingAllServiceImpl;

    @Operation(summary = "Создать новую настройку", description = "Создаёт новую настройку и возвращает её данные")
    @ApiResponse(responseCode = "201", description = "Новая настройка успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BankSettingDto.class)))
    @PostMapping
    public ResponseEntity<BankSettingDto> create(@Valid @RequestBody BankSettingDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bankSettingAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить настройку по ID", description = "Возвращает настройку по её уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Настройка найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BankSettingDto.class)))
    @ApiResponse(responseCode = "404", description = "Настройка не найдена")
    @GetMapping("/{id}")
    public ResponseEntity<BankSettingDto> findById(@Parameter(description = "ID настройки", example = "1")
                                                   @PathVariable("id") Long id) {
        return ResponseEntity.ok(bankSettingAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех настроек", description = "Возвращает все настройки из базы данных")
    @ApiResponse(responseCode = "200", description = "Список аккаунтов",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BankSettingDto.class)))
    @GetMapping
    public ResponseEntity<List<BankSettingDto>> findAll() {
        return ResponseEntity.ok(bankSettingAllServiceImpl.findAll());
    }

    @Operation(summary = "Обновить данные настройки", description = "Обновляет существующую настройку")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BankSettingDto.class)))
    @PutMapping
    public ResponseEntity<BankSettingDto> update(@Valid @RequestBody BankSettingDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(bankSettingAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить настройку", description = "Удаляет настройку по его ID")
    @ApiResponse(responseCode = "200", description = "Настройка успешно удалён")
    @ApiResponse(responseCode = "404", description = "Настройка не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@Parameter(description = "ID настройки", example = "1")
                                          @PathVariable("id") Long id) {
        boolean delete = bankSettingAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }
}
