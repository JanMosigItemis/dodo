package de.itemis.jmo.dodo.io;

import de.itemis.jmo.dodo.error.DodoException;

public interface DataSource extends AutoCloseable {

    /**
     * Read data. Blocks until data is ready.
     *
     * @return The data read or an empty array if no data is available anymore.
     * @throws DodoException In case of any I/O error.
     */
    byte[] read();
}
