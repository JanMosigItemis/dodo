/**
 *
 */
package de.itemis.jmo.dodo.io;

/**
 * A {@link ProgressListener} that knows when progress is to be considered "complete".
 *
 */
public class FinishableProgressListener implements ProgressListener<Long> {

    private final long upperBound;
    private final Runnable callback;

    private long currentProgress = 0;

    /**
     * Create a new instance.
     *
     * @param upperBound - If accumulated progress reaches this bound, progress will be considered
     *        'complete'.
     * @param callback - Run this if upperBound has been reached. Be aware that this will be called
     *        synchronously, so you better run expensive operations in a separate thread.
     */
    public FinishableProgressListener(long upperBound, Runnable callback) {
        this.upperBound = upperBound;
        this.callback = callback;
    }

    @Override
    public void updateProgress(Long progress) {
        currentProgress = currentProgress + progress;
        if (currentProgress >= upperBound) {
            callback.run();
        }
    }
}
