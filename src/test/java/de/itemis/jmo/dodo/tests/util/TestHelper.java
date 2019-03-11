package de.itemis.jmo.dodo.tests.util;

import static java.util.Objects.requireNonNull;

import com.google.common.base.Throwables;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

import de.itemis.jmo.dodo.util.InstantiationNotAllowedException;

/**
 * <p>
 * Some convenience methods that help with writing short and concise test code.
 * </p>
 */
public final class TestHelper {

    private TestHelper() {
        throw new InstantiationNotAllowedException();
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
        printWarning("WARN - " + message + ": " + cause.getClass().getSimpleName() + ": " + extractCauseMessage(cause));
    }

    /**
     * Print a warning with a default prefix to {@code stderr}.
     */
    public static void printWarning(String message) {
        System.err.println("WARN - " + message);
    }

    private static String attachExceptionInformation(String prefix, Throwable cause) {
        String causeMessage = extractCauseMessage(cause);
        return prefix + "\n" + cause.getClass().getSimpleName() + " - " + causeMessage + "\n" + Throwables.getStackTraceAsString(cause);
    }

    private static String extractCauseMessage(Throwable cause) {
        return cause.getMessage() == null ? "w/o further information" : cause.getMessage();
    }

    /**
     * Repeatedly run the provided action until either condition or timeout is reached.
     *
     * @param pollAction - Run this action.
     * @param condition - Success condition.
     * @param timeout - Precision is milliseconds.
     * @throws AssertionError - In case polling timed out.
     */
    public static <T> void pollUntil(Supplier<T> pollAction, Predicate<T> condition, Duration timeout) {
        long start = System.currentTimeMillis();
        while (!condition.test(pollAction.get())) {
            if (System.currentTimeMillis() - start >= timeout.toMillis()) {
                Assertions.fail("Polling value timed out.");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                printWarning("Was interrupted while polling a value.", e);
            }
        }
    }
}
