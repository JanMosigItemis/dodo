package de.itemis.jmo.dodo.model;

import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.io.Persistence;

/**
 * Local file system based {@link Persistence} implementation.
 */
public class DodoPersistence implements Persistence {

    @Override
    public void write(DataSource dataSource, Path targetPath) {
        try (OutputStream out = newOutputStream(targetPath, CREATE, TRUNCATE_EXISTING)) {
            byte[] buf = new byte[0];
            while ((buf = dataSource.read()).length > 0) {
                out.write(buf);
            }
        } catch (IOException e) {
            throw new DodoException("Error while writing data to local filesystem.", e);
        }
    }
}
