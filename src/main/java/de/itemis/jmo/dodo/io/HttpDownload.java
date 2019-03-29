package de.itemis.jmo.dodo.io;

import java.io.IOException;
import java.io.InputStream;

import de.itemis.jmo.dodo.error.DodoException;

public class HttpDownload implements DodoDownload {

    /**
     * Data is read in blocks of this size in bytes.
     */
    public static final int BLOCK_SIZE_BYTES = 8192;

    private final WrappedUrl downloadUrl;

    /**
     * Create new instance.
     */
    public HttpDownload(WrappedUrl url) {
        downloadUrl = url;
    }

    @Override
    public StreamDataSource getDataSource() {
        InputStream inputStream = null;
        try {
            inputStream = downloadUrl.openStream();
        } catch (IOException e) {
            throw new DodoException("Encountered error while opening a stream.", e);
        }

        return new StreamDataSource(inputStream, BLOCK_SIZE_BYTES);
    }

    @Override
    public long getSize() {
        return downloadUrl.getContentLength();
    }
}
