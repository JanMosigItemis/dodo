package de.itemis.jmo.dodo.io;

import static java.util.Arrays.copyOf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.util.InfiniteIterationOf;

/**
 * A {@link DataSource} that wraps an {@link InputStream} and reads with a fixed block size.
 */
public class StreamDataSource implements DataSource {
    private static final Logger LOG = LoggerFactory.getLogger(StreamDataSource.class);

    private static final int DEFAULT_BLOCK_SIZE_BYTES = 8 * 1024;
    private static final int ALWAYS_START_READING_AT_0 = 0;

    private final InputStream stream;
    private final Iterator<Integer> blockSizeStrategy;

    /**
     * Create a new instance.
     *
     * @param stream - The wrapped stream is created lazily so that resources may not be blocked for
     *        an unnecessarily long amount of time.
     */
    public StreamDataSource(InputStream stream) {
        this(stream, new InfiniteIterationOf(8 * 1024 * 1024));
    }

    /**
     * Do not use.
     *
     * @param stream
     * @param blockSizeStrategy
     */
    StreamDataSource(InputStream stream, Iterator<Integer> blockSizeStrategy) {
        this.stream = stream;
        this.blockSizeStrategy = blockSizeStrategy;
    }

    @Override
    public void close() throws Exception {
        stream.close();
    }

    @Override
    public byte[] read() {
        int blockSizeBytes = blockSizeStrategy.next();
        if (blockSizeBytes < 1) {
            LOG.warn("Got block size of " + blockSizeBytes + " bytes. Using default block size: " + DEFAULT_BLOCK_SIZE_BYTES + " bytes.");
            blockSizeBytes = DEFAULT_BLOCK_SIZE_BYTES;
        }

        byte[] buf = new byte[blockSizeBytes];
        int byteCount = 0;
        try {
            byteCount = stream.readNBytes(buf, ALWAYS_START_READING_AT_0, blockSizeBytes);
        } catch (IOException e) {
            throw new DodoException("Encountered error while reading from stream.", e);
        }

        return copyOf(buf, byteCount);
    }


}
