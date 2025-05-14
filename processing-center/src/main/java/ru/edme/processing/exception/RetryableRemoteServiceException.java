package ru.edme.processing.exception;

// Базовое исключение для ошибок, которые можно повторить (например, временные сбои)
public class RetryableRemoteServiceException extends RemoteServiceException {

    public RetryableRemoteServiceException(String message) {
        super(message);
    }

    public RetryableRemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
