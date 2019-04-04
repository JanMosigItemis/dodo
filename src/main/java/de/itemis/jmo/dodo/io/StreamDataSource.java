package de.itemis.jmo.dodo.io;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.copyOf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * A {@link DataSource} that wraps an {@link InputStream}.
 */
public class StreamDataSource implements DataSource {

    private static final int ALWAYS_START_READING_AT_0 = 0;

    private final InputStream stream;
    private final Iterator<Integer> blockSizeStrategy;

    /**
     * Construct a new instance.
     *
     * @param stream - Work with this stream.
     * @param blockSizeStrategy - Data is read in blocks. Each read op uses one value from this
     *        iterator for the next block size.
     */
    public StreamDataSource(InputStream stream, Iterator<Integer> blockSizeStrategy) {
        this.stream = stream;
        this.blockSizeStrategy = blockSizeStrategy;
    }

    @Override
    public void close() throws Exception {
        stream.close();
    }

    @Override
    public byte[] read() {
        int blockSize = blockSizeStrategy.next();
        checkArgument(blockSize > 0, "Block size must be greater than zero.");

        byte[] buf = new byte[blockSize];
        int byteCount = 0;
        try {
            byteCount = stream.readNBytes(buf, ALWAYS_START_READING_AT_0, blockSize);
        } catch (IOException e) {
            throw new DodoException("Encountered error while reading from stream.", e);
        }

        return copyOf(buf, byteCount);
    }
}
