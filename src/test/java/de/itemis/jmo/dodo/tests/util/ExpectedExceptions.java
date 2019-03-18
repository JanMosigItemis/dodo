package de.itemis.jmo.dodo.tests.util;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import java.io.IOException;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.util.InstantiationNotAllowedException;

/**
 * A collection of expected exceptions that ease test implementation and carry around a menaingful
 * message that aims at prohibiting confusion with unexpected exceptions in test logs.
 */
public final class ExpectedExceptions {

    /**
     * A pacifying message for expected test exceptions.
     */
    public static final String EXCEPTION_MESSAGE = "Expected exception. Please ignore.";
    public static final RuntimeException EXPECTED_RUNTIME_EXCEPTION = new RuntimeException(EXCEPTION_MESSAGE);
    public static final DodoException DODO_EXCEPTION = new DodoException(EXCEPTION_MESSAGE);
    public static final IOException EXPECTED_IO_EXCEPTION = new IOException(EXCEPTION_MESSAGE);
    public static final Exception EXPECTED_CHECKED_EXCEPTION = new Exception(EXCEPTION_MESSAGE);

    private ExpectedExceptions() {
        throw new InstantiationNotAllowedException();
    }

    /**
     * Like {@link Assertions#assertThatThrownBy(ThrowingCallable)} but performs the required checks
     * on its own, thus saving boilerplate code.
     *
     * <pre>
     * assertThatThrownBy(callable)
     *     .isInstanceOf(expectedException.getClass())
     *     .hasMessage(expectedException.getMessage())
     *     .hasCause(expectedException.getCause());
     * </pre>
     *
     * becomes
     *
     * <pre>
     * assertThatThrownBy(callable, expectedException);
     * </pre>
     *
     * @return The return value of {@link Assertions#assertThatThrownBy(ThrowingCallable)} for
     *         continued chaining.
     */
    public static AbstractThrowableAssert<?, ? extends Throwable> assertThatThrownBy(ThrowingCallable callable, Exception expectedException) {
        return Assertions.assertThatThrownBy(callable)
            .isInstanceOf(expectedException.getClass())
            .hasMessage(expectedException.getMessage())
            .hasCause(expectedException.getCause());
    }
}
