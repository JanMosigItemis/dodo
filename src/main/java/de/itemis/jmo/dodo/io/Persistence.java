package de.itemis.jmo.dodo.io;

import java.nio.file.Path;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * Abstracts away persistence details.
 */
public interface Persistence {

    /**
     * Like {@link #write(DataSource, Path)} but takes a {@link ProgressListener} that is used to
     * inform about write progress. Progress will be reported as "bytes written", i. e. the passed
     * amount is an increasing value.
     *
     * @param dataSource - Use this object to read data.
     * @param targetPath - Must point to the target.
     * @param listener - Callback
     *
     * @throws DodoException - In case reading or writing encountered an error.
     */
    void write(DataSource dataSourceMock, Path targetPath, ProgressListener<Long> listener);

    /**
     * Provide a {@link DataSource} instance that can be used to read data from the specified
     * {@code path}.
     *
     * @param path - Read data from this path.
     * @return A new instance of {@link DataSource}.
     * @throws DodoException in case opening a source on {@code path} runs into an error.
     */
    DataSource read(Path path);
}
