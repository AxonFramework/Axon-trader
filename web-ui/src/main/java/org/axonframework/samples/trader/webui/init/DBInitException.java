package org.axonframework.samples.trader.webui.init;

/**
 * Exception thrown when having a problem during data initialization
 */
public class DBInitException extends RuntimeException {
    public DBInitException(String message) {
        super(message);
    }

    public DBInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
