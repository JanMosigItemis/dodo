package de.itemis.jmo.dodo.tests;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Throwables;

import org.junit.Assert;

/**
 * <p>
 * Some convenience methods that help with writing short and concise test code.
 * </p>
 */
public final class TestHelper {

    private TestHelper() {
        throw new UnsupportedOperationException("Instantiation not supported.");
    }

    /**
     * Like {@link Assert#fail(String)} but does also attach {@code cause}'s message and stacktrace.
     */
    public static void fail(String message, Throwable cause) {
        requireNonNull(message, "message");
        requireNonNull(cause, "cause");

        Assert.fail(attachExceptionInformation(message, cause));
    }

    /**
     * In some cases, failing in case of an {@link Exception} is clumsy, i. e. if a test cleanup
     * fails. In those cases, a warning will be printed to std.err instead.
     */
    public static void printWarning(String message, Throwable cause) {
        System.err.println("WARN - " + message + ": " + cause.getClass().getSimpleName() + ": " + extractCauseMessage(cause));
    }

    private static String attachExceptionInformation(String prefix, Throwable cause) {
        String causeMessage = extractCauseMessage(cause);
        return prefix + "\n" + cause.getClass().getSimpleName() + " - " + causeMessage + "\n" + Throwables.getStackTraceAsString(cause);
    }

    private static String extractCauseMessage(Throwable cause) {
        return cause.getMessage() == null ? "w/o further information" : cause.getMessage();
    }
}
