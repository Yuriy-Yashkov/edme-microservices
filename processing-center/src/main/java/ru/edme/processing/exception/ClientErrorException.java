package ru.edme.processing.exception;

// Ошибка клиента — 4xx (например, 404, 400)
public class ClientErrorException extends RemoteServiceException {

    public ClientErrorException(String message) {
        super(message);
    }

    public ClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
