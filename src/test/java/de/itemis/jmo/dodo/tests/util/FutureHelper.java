package de.itemis.jmo.dodo.tests.util;

import static de.itemis.jmo.dodo.util.Sneaky.throwThat;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Static helper class with convenience methods that help keeping test code clean of {@link Future}
 * details.
 */
public final class FutureHelper {

    private FutureHelper() {
        throw new UnsupportedOperationException("Instantiation not permitted.");
    }

    /**
     * Wait for the {@code latch} to become zero.
     *
     * @param latch - Wait for this latch.
     * @param timeout - Wait for as max as this timeout. Precision is milliseconds.
     * @return {@code true} If the count reached zero and {@code false} if the waiting time elapsed
     *         before the count reached zero.
     * @throws RuntimeException In case waiting was interrupted. The original
     *         {@link InterruptedException} will be attached as cause.
     */
    public static boolean waitForLatch(CountDownLatch latch, Duration timeout) {
        try {
            return latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Was interrupted while waiting on latch.", e);
        }
    }

    /**
     * Assert that the {@code latch} eventually becomes zero.
     *
     * @param latch - Wait on this latch.
     * @param timeout - Wait for as max as this timeout. Precision is milliseconds.
     * @throws AssertionError If waiting timed out.
     * @throws RuntimeException In case waiting was interrupted. The original
     *         {@link InterruptedException} will be attached as cause.
     */
    public static void assertLatch(CountDownLatch latch, Duration timeout) {
        assertTrue("Waiting on latch to become zero timed out.", waitForLatch(latch, timeout));
    }

    /**
     * <p>
     * Waits on the provided {@code future} until it returns a result, times out, is cancelled or
     * errors out, whatever happens first.
     * </p>
     * <p>
     * On {@link InterruptedException}, the thread interrupt flag is restored and a
     * {@link RuntimeException} with a meaningful error message is thrown with the original
     * {@link InterruptedException} attached as cause.
     * </p>
     * <p>
     * On timeout, a {@link RuntimeException} with a meaningful error message is thrown with the
     * original {@link TimeoutException} attached as cause.
     * </p>
     * <p>
     * On {@link ExecutionException}, the exception's cause will be thrown or the exception itself
     * if it happens to have no cause.
     * </p>
     * <p>
     * On cancellation, a {@link RuntimeException} with a meaningful error message is thrown with
     * the original {@link CancellationException} attached as cause.
     * </p>
     * <p>
     * <b>Be aware:</b> This method may throw checked exceptions, even though it does not declare
     * them!
     * </p>
     *
     * @param <T> - Return type of provided {@code future}.
     * @param opName - A meaningful name of the future's operation. Used in exception messages.
     * @param timeout - Wait for as max as this timeout. Precision is milliseconds.
     * @param future - Wait on this future.
     * @return The future's result if any.
     * @throws RuntimeException - See above for details.
     * @throws Exception - In case of an {@link ExecutionException}. See above for details.
     */
    public static <T> T waitForFuture(String opName, Duration timeout, Future<T> future) {
        T result = null;

        try {
            result = future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation '" + opName + "' was interrupted.", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throwThat(cause);
        } catch (TimeoutException e) {
            throw new RuntimeException("Operation '" + opName + "' timed out.", e);
        } catch (CancellationException e) {
            throw new RuntimeException("Operation '" + opName + "' was canceled.", e);
        }

        return result;
    }
}
