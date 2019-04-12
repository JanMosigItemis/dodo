package de.itemis.jmo.dodo.io;

import java.io.InputStream;
import java.util.Iterator;

/**
 * A {@link DataSource} that wraps an {@link InputStream} and reads in varying block sizes.
 */
public class VariableBlockSizeStreamDataSource extends StreamDataSource {

    /**
     * Construct a new instance.
     *
     * @param stream - Work with this stream.
     * @param blockSizeStrategy - Data is read in blocks. Each read op uses one value from this
     *        iterator for the next block size.
     */
    public VariableBlockSizeStreamDataSource(InputStream stream, Iterator<Integer> blockSizeStrategy) {
        super(stream, blockSizeStrategy);
    }
}
