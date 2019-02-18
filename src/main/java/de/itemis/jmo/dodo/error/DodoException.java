package de.itemis.jmo.dodo.error;

/**
 * Main implementation for all exceptions that may be risen by Dodo's API.
 */
public class DodoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new instance.
     *
     * @param msg - Exception's message.
     */
    public DodoException(String msg) {
        super(msg);
    }

    /**
     * Create a new instance.
     *
     * @param msg - Exception's message.
     * @param cause - Exception's cause.
     */
    public DodoException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
