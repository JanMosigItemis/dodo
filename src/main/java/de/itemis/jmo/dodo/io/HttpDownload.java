package de.itemis.jmo.dodo.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import de.itemis.jmo.dodo.error.DodoException;

public class HttpDownload implements DodoDownload {

    private final WrappedUrl downloadUrl;
    private final Iterator<Integer> blockSizeStrategy;

    /**
     * Create new instance.
     *
     * @param blockSizeStrategy - Data is read in blocks of bytes. This strategy provides the
     *        blocksize for each read operation.
     */
    public HttpDownload(WrappedUrl url, Iterator<Integer> blockSizeStrategy) {
        downloadUrl = url;
        this.blockSizeStrategy = blockSizeStrategy;
    }

    @Override
    public VariableBlockSizeStreamDataSource getDataSource() {
        InputStream inputStream = null;
        try {
            inputStream = downloadUrl.openStream();
        } catch (IOException e) {
            throw new DodoException("Encountered error while opening a stream.", e);
        }

        return new VariableBlockSizeStreamDataSource(inputStream, blockSizeStrategy);
    }

    @Override
    public long getSize() {
        return downloadUrl.getContentLength();
    }
}
