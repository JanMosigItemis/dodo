package de.itemis.jmo.dodo.io;

import java.io.InputStream;
import java.nio.file.Path;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * Abstracts away persistence details.
 */
public interface Persistence {

    /**
     * Write all data from {@code dataSource} to the file that {@code targetPath} points to.
     *
     * @param targetPath - Must point to the target file.
     * @param dataSource - Read data from this {@link InputStream}. Stream will be at end if this
     *        method returns normally.
     * @throws DodoException - In case reading or writing encountered an error.
     */
    void write(Path targetPath, InputStream dataSource);


}
