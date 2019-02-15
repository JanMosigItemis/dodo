package de.itemis.jmo.dodo.tests.util;

/**
 * A collection of expected exceptions that ease test implementation and carry around a menaingful
 * message that aims at prohibiting confusion with unexpected exceptions in test logs.
 */
public final class ExpectedExceptions {

    public static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException("Expected exception. Please ignore.");

    private ExpectedExceptions() {
        throw new UnsupportedOperationException("Instantiation not allowed.");
    }
}
