package de.itemis.jmo.dodo.io;

import static java.util.Arrays.copyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VariableStreamDataSourceTest {

    private static final String DATA_STR = "Hello World!";
    private static final byte[] DATA = DATA_STR.getBytes(StandardCharsets.UTF_8);
    private static final int DATA_LENGTH_BYTES = DATA.length;

    private InputStream realStream;
    private InputStream streamMock;
    private Iterator<Integer> blockSizeStrategyMock;

    private VariableBlockSizeStreamDataSource underTest;

    // Mockito's mock API does not go well with generics. However, everything is safe here.
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() throws Exception {
        blockSizeStrategyMock = mock(Iterator.class);
        streamMock = mock(InputStream.class);

        constructUnderTest(DATA_LENGTH_BYTES);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (realStream != null) {
            realStream.close();
        }
    }

    @Test
    public void read_returns_all_data_if_block_size_equals_data() {
        nextBlockSizeIs(DATA_LENGTH_BYTES);

        byte[] result = underTest.read();

        assertThat(result).as("Encountered unexpected read result").isEqualTo(DATA);
    }

    @Test
    public void read_returns_block_size_data_if_block_size_smaller_data() {
        int blockSize = DATA_LENGTH_BYTES - 1;
        nextBlockSizeIs(blockSize);

        byte[] actualResult = underTest.read();

        assertThat(actualResult).as("Encountered unexpected read result").isEqualTo(copyOf(DATA, blockSize));
    }

    @Test
    public void read_returns_all_data_if_block_size_greater_data() {
        nextBlockSizeIs(DATA_LENGTH_BYTES + 1);

        byte[] actualResult = underTest.read();

        assertThat(actualResult).as("Encountered unexpected read result").isEqualTo(DATA);
    }

    @Test
    public void reads_all_data_if_block_size_is_multiple_of_length_and_read_is_done_multiple_times() {
        int factor = 2;
        int blockSize = DATA_LENGTH_BYTES / factor;
        nextBlockSizeIs(blockSize);

        var readBytes = readInLoop(factor);

        assertThat(readBytes).isEqualTo(DATA);
    }

    @Test
    public void constructing_with_block_size_zero_raises_illegal_argument() {
        nextBlockSizeIs(0);

        var result = readInLoop(1);

        assertThat(result).isEqualTo(DATA);
    }

    @Test
    public void providing_negative_block_size_during_read_causes_read_with_default_block_size() {
        nextBlockSizeIs(-1);

        var result = readInLoop(1);

        assertThat(result).isEqualTo(DATA);
    }

    @Test
    public void reading_with_varying_block_sizes_yields_correct_result() {
        int first = 1;
        int second = 2;
        int third = 3;
        int tail = DATA_LENGTH_BYTES - first - second - third;
        nextBlockSizeIs(first, second, third, tail);

        byte[] result = readInLoop(4);

        assertThat(result).isEqualTo(DATA);
    }

    /*
     * ##########################
     *
     * Start private helper code.
     *
     * ##########################
     */

    private void streamReadOpAnswer(Answer<Integer> answer) throws IOException {
        doAnswer(answer).when(streamMock).readNBytes(any(byte[].class), anyInt(), anyInt());
    }

    private byte[] readInLoop(int iterations) {
        var readBytes = new ArrayList<Byte>();
        for (var i = 0; i < iterations; i++) {
            readBytes.addAll(toList(underTest.read()));
        }
        byte[] result = new byte[readBytes.size()];
        for (var i = 0; i < readBytes.size(); i++) {
            result[i] = readBytes.get(i);
        }
        return result;
    }

    private List<Byte> toList(byte[] bytes) {
        List<Byte> result = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            result.add(bytes[i]);
        }

        return result;
    }

    private void constructUnderTest(int blockSizeBytes) throws Exception {
        nextBlockSizeIs(blockSizeBytes);

        realStream = new ByteArrayInputStream(DATA);

        streamReadOpAnswer(invocation -> {
            byte[] buf = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            return realStream.readNBytes(buf, offset, len);
        });

        underTest = new VariableBlockSizeStreamDataSource(streamMock, blockSizeStrategyMock);
    }

    private void nextBlockSizeIs(Integer... blockSizes) {
        int head = blockSizes[0];
        Integer[] tail = blockSizes.length > 1 ? Arrays.copyOfRange(blockSizes, 1, blockSizes.length) : new Integer[0];
        when(blockSizeStrategyMock.next()).thenReturn(head, tail);
    }
}
