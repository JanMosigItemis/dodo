package de.itemis.jmo.dodo.tests.util;

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
    public static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException(EXCEPTION_MESSAGE);
    public static final DodoException DODO_EXCEPTION = new DodoException(EXCEPTION_MESSAGE);
    public static final IOException EXPECTED_IO_EXCEPTION = new IOException(EXCEPTION_MESSAGE);

    private ExpectedExceptions() {
        throw new InstantiationNotAllowedException();
    }
}
