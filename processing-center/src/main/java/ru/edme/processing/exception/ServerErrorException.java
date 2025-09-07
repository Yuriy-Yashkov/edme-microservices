package ru.edme.processing.exception;

// Ошибка от внешнего сервиса с кодом 5xx (серверная ошибка)
public class ServerErrorException extends RetryableRemoteServiceException {

    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
