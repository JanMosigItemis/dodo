package de.itemis.jmo.dodo.tests.util;

import java.util.Iterator;

/**
 * Provides an infinite iteration of block sizes which can be altered during test runtime, so that
 * different block sizes can be simulated. This may especially be important for calculating reliable
 * percentages when stalling downloads.
 */
public final class DownloadBlockSizeStrategyForTests implements Iterator<Integer> {

    private static final int DEFAULT_BLOCK_SIZE_BYTES = 8 * 1024;

    private int currentBlockSizeBytes;

    public DownloadBlockSizeStrategyForTests() {
        reset();
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        return currentBlockSizeBytes;
    }

    /**
     * All next values will be {@code nextValue}.
     */
    public void setNext(int nextValue) {
        currentBlockSizeBytes = nextValue;
    }

    /**
     * Reset the iteration to the default value.
     */
    public final void reset() {
        currentBlockSizeBytes = DEFAULT_BLOCK_SIZE_BYTES;
    }
}
