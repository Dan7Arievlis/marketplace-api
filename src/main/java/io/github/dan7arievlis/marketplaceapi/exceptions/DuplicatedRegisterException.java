package io.github.dan7arievlis.marketplaceapi.exceptions;

public class DuplicatedRegisterException extends RuntimeException {
    public DuplicatedRegisterException(String message) {
        super(message);
    }

    public DuplicatedRegisterException(String message, Throwable cause) {
        super(message, cause);
    }
}
