package de.itemis.jmo.dodo.io;

/**
 * Generic interface for components that want to be informed about some kind of progress, be it
 * relative or absolute.
 *
 * @param <T> - Progress is reported as this type.
 */
public interface ProgressListener<T> {

    /**
     * <p>
     * Inform the listener about progress. This could be absolut, e. g. 50% progress modeled as a
     * double. Or relative, e. g. 3 bytes read modeled as a long.
     * </p>
     * <p>
     * Implementation must not block the caller for an unreasonably long amount of time. If in
     * doubt, perform work in another thread.
     * </p>
     *
     * @param progress - Reported progress.
     */
    void updateProgress(T progress);
}
