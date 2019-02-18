package de.itemis.jmo.dodo.tests.testfx;

import java.nio.file.Path;

public interface DodoTestsJunit5Lifecycle {

    /**
     * Performs initialization before the first test method is run.
     *
     * @throws AssertionError if initialization threw an {@link Exception}.
     */
    default void beforeAll() {};

    /**
     * Performs cleanup after the last test method has been run. Prints any {@link Exception} thrown
     * during cleanup to {@code stderr}. Does always return in order to make sure, cleanup does not
     * fail already passed tests.
     */
    default void afterAll() {};

    /**
     * Performs initialization before each test method.
     *
     * @throws AssertionError if initialization threw an {@link Exception}.
     */
    default void beforeEach() {};

    /**
     * Performs cleanup after each test method. Prints any {@link Exception} thrown during cleanup
     * to {@code stderr}. Does always return in order to make sure, cleanup does not fail already
     * passed tests.
     */
    default void afterEach() {};

    /**
     * Hook to pass a temporary test directory to a test driver. The driver is supposed to use this
     * directory to store temporary test data. The instantiator of the driver is supposed to take
     * care of creation and deletion of this directory.
     *
     * @param tmpDirPath - Must point to an existing directory.
     */
    default void initPersistence(Path tmpDirPath) {};

}