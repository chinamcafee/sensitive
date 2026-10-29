package com.github.houbb.chars.scan.exception;

/**
 * 线程异常
 *
 * @since 1.13.0
 */
public class CharsScanException extends RuntimeException {

    public CharsScanException() {
    }

    public CharsScanException(String message) {
        super(message);
    }

    public CharsScanException(String message, Throwable cause) {
        super(message, cause);
    }

    public CharsScanException(Throwable cause) {
        super(cause);
    }

}
