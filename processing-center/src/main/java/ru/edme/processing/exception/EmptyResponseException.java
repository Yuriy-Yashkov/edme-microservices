package ru.edme.processing.exception;

// Ошибка, если получен успешный статус (2xx), но тело ответа — пустое
public class EmptyResponseException extends RetryableRemoteServiceException {

    public EmptyResponseException(String message) {
        super(message);
    }

    public EmptyResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
