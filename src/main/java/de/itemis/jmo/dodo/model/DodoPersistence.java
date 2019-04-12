package de.itemis.jmo.dodo.model;

import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.io.Persistence;
import de.itemis.jmo.dodo.io.ProgressListener;
import de.itemis.jmo.dodo.io.StreamDataSource;

/**
 * Local file system based {@link Persistence} implementation.
 */
public class DodoPersistence implements Persistence {

    @Override
    public void write(DataSource dataSource, Path targetPath, ProgressListener<Long> listener) {
        try (OutputStream out = newOutputStream(targetPath, CREATE, TRUNCATE_EXISTING)) {
            byte[] buf = null;
            long totalBytesWritten = 0;
            while ((buf = dataSource.read()).length > 0) {
                out.write(buf);
                out.flush();
                totalBytesWritten = totalBytesWritten + buf.length;
                listener.updateProgress(totalBytesWritten);
            }
        } catch (IOException e) {
            throw new DodoException("Error while writing data to local filesystem.", e);
        }
    }

    @Override
    public DataSource read(Path path) {
        InputStream stream = null;
        try {
            stream = Files.newInputStream(path);
        } catch (IOException e) {
            throw new DodoException("Could not create data source", e);
        }
        return new StreamDataSource(stream);
    }
}
