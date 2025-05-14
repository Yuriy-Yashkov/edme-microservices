package ru.edme.processing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Глобальный обработчик исключений для всех контроллеров.
 * Он ловит пользовательские исключения, возникающие при вызове внешних сервисов,
 * и возвращает стандартизированный HTTP-ответ.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }

    /**
     * Обрабатывает ошибки клиента (HTTP 4xx).
     * Повторять запрос не имеет смысла.
     */
    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<String> handleClientError(ClientErrorException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Client error: " + ex.getMessage());
    }

    /**
     * Обрабатывает ошибки, которые можно повторить.
     * Возвращает статус 503 — "временная недоступность".
     */
    @ExceptionHandler(RetryableRemoteServiceException.class)
    public ResponseEntity<String> handleRetryableException(RetryableRemoteServiceException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Temporary issue: " + ex.getMessage());
    }

    /**
     * Обрабатывает все остальные ошибки при вызове удалённых сервисов.
     * Это может быть что-то неожиданное, не относящееся к retryable.
     */
    @ExceptionHandler(RemoteServiceException.class)
    public ResponseEntity<String> handleRemoteServiceException(RemoteServiceException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Service error: " + ex.getMessage());
    }
}
