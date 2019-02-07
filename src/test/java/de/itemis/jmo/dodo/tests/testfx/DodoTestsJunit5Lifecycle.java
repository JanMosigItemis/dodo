package de.itemis.jmo.dodo.tests.testfx;



public interface DodoTestsJunit5Lifecycle {

    /**
     * Performs initialization before each test.
     *
     * @throws AssertionError if initialization threw an {@link Exception}.
     */
    void beforeEach();

    /**
     * Performs cleanup after each test method. Prints any {@link Exception} thrown during cleanup
     * to {@code stderr}. Does always return in order to make sure, cleanup does not fail already
     * passed tests.
     */
    void afterEach();

}