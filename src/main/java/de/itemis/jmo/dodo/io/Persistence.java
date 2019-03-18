package de.itemis.jmo.dodo.io;

import java.nio.file.Path;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * Abstracts away persistence details.
 */
public interface Persistence {

    /**
     * Get all data from the provided {@code source} and write it to the target that
     * {@code targetPath} points to. Replaces existing files.
     *
     * @param dataSource - Use this object to read data.
     * @param targetPath - Must point to the target.
     *
     * @throws DodoException - In case reading or writing encountered an error.
     */
    void write(DataSource dataSource, Path targetPath);
}
