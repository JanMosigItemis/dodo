package de.itemis.jmo.dodo.io;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.copyOf;

import java.io.IOException;
import java.io.InputStream;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * A {@link DataSource} that wraps an {@link InputStream}.
 */
public class StreamDataSource implements DataSource {

    private static final int ALWAYS_START_WRITING_AT_0 = 0;

    private final InputStream stream;
    private final int blockSizeBytes;

    /**
     * Construct a new instance.
     *
     * @param stream - Work with this stream.
     * @param blockSizeBytes - Each read block is of this size.
     */
    public StreamDataSource(InputStream stream, int blockSizeBytes) {
        checkArgument(blockSizeBytes > 0, "Block size must be greater than zero.");
        this.stream = stream;
        this.blockSizeBytes = blockSizeBytes;
    }

    @Override
    public void close() throws Exception {
        stream.close();
    }

    @Override
    public byte[] read() {
        byte[] buf = new byte[blockSizeBytes];

        int byteCount = 0;
        try {
            byteCount = stream.readNBytes(buf, ALWAYS_START_WRITING_AT_0, blockSizeBytes);
        } catch (IOException e) {
            throw new DodoException("Encountered error while reading from stream.", e);
        }

        return copyOf(buf, byteCount);
    }
}
