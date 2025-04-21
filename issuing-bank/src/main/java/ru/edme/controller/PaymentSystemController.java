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
import ru.edme.dto.PaymentSystemDto;
import ru.edme.model.Account;
import ru.edme.service.AllService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/issuing-bank/payment-systems")
@Tag(name = "PaymentSystem Controller", description = "Управление платежной системой")
public class PaymentSystemController {

    private final AllService<PaymentSystemDto, Long> paymentSystemAllServiceImpl;

    @Operation(summary = "Создать новую платежную систему", description = "Создаёт новый платежную систему и возвращает её данные")
    @ApiResponse(responseCode = "201", description = "Платежная система успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PostMapping
    public ResponseEntity<PaymentSystemDto> create(@Valid @RequestBody PaymentSystemDto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentSystemAllServiceImpl.save(entity));
    }

    @Operation(summary = "Получить платежную систему по ID", description = "Возвращает платежную систему по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Платежная система найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @ApiResponse(responseCode = "404", description = "Платежная система не найден")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentSystemDto> findById(@Parameter(description = "ID платежной системы", example = "1")
                                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentSystemAllServiceImpl.findById(id));
    }

    @Operation(summary = "Получить список всех платежных систем", description = "Возвращает все платёжные системы из базы данных")
    @ApiResponse(responseCode = "200", description = "Список платежных систем",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @GetMapping
    public ResponseEntity<List<PaymentSystemDto>> findAll() {
        return ResponseEntity.ok(paymentSystemAllServiceImpl.findAll());
    }

    @Operation(summary = "Обновить данные платежной системы", description = "Обновляет существующую платежную систему")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Account.class)))
    @PutMapping
    public ResponseEntity<PaymentSystemDto> update(@Valid @RequestBody PaymentSystemDto entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(paymentSystemAllServiceImpl.update(entity));
    }

    @Operation(summary = "Удалить платежную систему", description = "Удаляет платежную систему по его ID")
    @ApiResponse(responseCode = "200", description = "Платёжная система успешно удалена")
    @ApiResponse(responseCode = "404", description = "Платёжная система не найдена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID платежной системы", example = "1")
                                       @PathVariable("id") Long id) {
        boolean delete = paymentSystemAllServiceImpl.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
