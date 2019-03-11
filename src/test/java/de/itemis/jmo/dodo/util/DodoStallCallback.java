package de.itemis.jmo.dodo.util;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * Used to synchronize tests with an async operation until a specified stall point has been reached.
 */
public final class DodoStallCallback {

    private CountDownLatch stallLatch = new CountDownLatch(1);

    /**
     * Block the calling thread until a previously specified stall point has been reached or the
     * timeout is reached.
     */
    public void waitUntilStallPoint(Duration timeout) {
        assertTimeoutPreemptively(timeout, () -> stallLatch.await());
    }

    /**
     * Call this in order to signal, that the stall point has been reached. All threads waiting on
     * {@link #waitUntilStallPoint(Duration)} will be released.
     */
    public void stallPointReached() {
        stallLatch.countDown();
    }
}
