package de.itemis.jmo.dodo.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.io.Persistence;

/**
 * Local file system based {@link Persistence} implementation.
 */
public class DodoPersistence implements Persistence {

    @Override
    public void write(Path targetPath, InputStream dataSource) {
        try {
            Files.copy(dataSource, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DodoException("Error while writing data to local filesystem.", e);
        }
    }
}
