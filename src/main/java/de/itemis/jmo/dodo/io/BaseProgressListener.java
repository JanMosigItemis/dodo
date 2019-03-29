/**
 *
 */
package de.itemis.jmo.dodo.io;

public class BaseProgressListener<T> implements ProgressListener<T> {

    private static final ProgressListener<?> NOP = new BaseProgressListener<>();

    // Doing a "static" cast here. As long as NOP's type doesn't change, it is safe.
    @SuppressWarnings("unchecked")
    public static final <V> ProgressListener<V> nop() {
        return (ProgressListener<V>) NOP;
    }

    @Override
    public void updateProgress(T progress) {

    }
}
