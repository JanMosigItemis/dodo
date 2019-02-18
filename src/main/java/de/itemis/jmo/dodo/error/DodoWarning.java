package de.itemis.jmo.dodo.error;

/**
 * A warning is an exception that may be ignored / just logged / etc. if catched. It indicates, that
 * the underlying error is not that fatal and the system will most likely continue to run without
 * error.
 */
public class DodoWarning extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construct a new instance.
     *
     * @param msg - Warning message.
     * @param cause - The {@link Exception} that caused the warning. Must not be {@code null}.
     */
    public DodoWarning(String msg, Throwable cause) {
        super(msg, cause);
    }
}
