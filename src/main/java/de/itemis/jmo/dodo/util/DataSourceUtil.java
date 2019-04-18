package de.itemis.jmo.dodo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.itemis.jmo.dodo.io.DataSource;

/**
 * Some convenience methods to ease dealing with {@link DataSource} instances.
 */
public final class DataSourceUtil {

    private DataSourceUtil() {
        throw new InstantiationNotAllowedException();
    }

    /**
     * Call {@link DataSource#close()} on the provided {@code source} and log any error that may
     * occur. This method is guaranteed to return.
     *
     * @param client - Log error with {@link Logger} of this class.
     * @param source - The {@link DataSource} to be closed.
     */
    public static void safeClose(Class<?> client, DataSource source) {
        try {
            source.close();
        } catch (Exception e) {
            LoggerFactory.getLogger(client).warn("Encountered unexpected error while closing dataSource.", e);
        }
    }
}
