package de.itemis.jmo.dodo.util;

import java.util.Iterator;

/**
 * An {@link Iterator} that returns an infinite amount of {@link Long} values. All values will be
 * the same. Can be used as a simple "always the same value" strategy.
 */
public class InfiniteIterationOf implements Iterator<Integer> {

    private final int value;

    /**
     * Create a new instance.
     *
     * @param value - {@link #next()} will always return this value.
     */
    public InfiniteIterationOf(int value) {
        this.value = value;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        return value;
    }
}
