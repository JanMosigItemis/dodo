package de.itemis.jmo.dodo.io;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.EXPECTED_IO_EXCEPTION;
import static java.util.Arrays.copyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.tests.util.ExpectedExceptions;

public class StreamDataSourceTest {

    private static final String DATA_STR = "Hello World!";
    private static final byte[] DATA = DATA_STR.getBytes(StandardCharsets.UTF_8);
    private static final int DATA_LENGTH_BYTES = DATA.length;

    private InputStream realStream;
    private InputStream streamMock;

    private StreamDataSource underTest;

    @BeforeEach
    public void setUp() throws Exception {
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
    public void close_calls_close_on_wrapped_stream() throws Exception {
        underTest.close();

        verify(streamMock).close();
    }

    @Test
    public void exceptions_during_close_are_propagated_as_is() throws Exception {
        doThrow(EXPECTED_IO_EXCEPTION).when(streamMock).close();
        assertThatThrownBy(() -> underTest.close()).isSameAs(EXPECTED_IO_EXCEPTION);
    }

    @Test
    public void read_returns_a_byte_array() {
        byte[] result = underTest.read();

        assertThat(result).isNotNull();
    }

    @Test
    public void read_returns_all_data_if_block_size_equals_data() {
        byte[] result = underTest.read();

        assertThat(result).as("Encountered unexpected read result").isEqualTo(DATA);
    }

    @Test
    public void read_returns_block_size_data_if_block_size_smaller_data() throws Exception {
        int blockSize = DATA_LENGTH_BYTES - 1;
        constructUnderTest(blockSize);

        byte[] actualResult = underTest.read();

        assertThat(actualResult).as("Encountered unexpected read result").isEqualTo(copyOf(DATA, blockSize));
    }

    @Test
    public void read_returns_all_data_if_block_size_greater_data() throws Exception {
        constructUnderTest(DATA_LENGTH_BYTES + 1);

        byte[] actualResult = underTest.read();

        assertThat(actualResult).as("Encountered unexpected read result").isEqualTo(DATA);
    }

    @Test
    public void reads_all_data_if_block_size_is_multiple_of_length_and_read_is_done_multiple_times() throws Exception {
        int factor = 2;
        int blockSize = DATA_LENGTH_BYTES / factor;
        constructUnderTest(blockSize);

        var readBytes = readInLoop(factor);

        assertThat(readBytes).isEqualTo(DATA);
    }

    @Test
    public void returns_empty_array_if_stream_is_at_end() throws Exception {
        streamReadOpAnswer(invocation -> 0);

        byte[] result = underTest.read();

        assertThat(result).isEmpty();
    }

    @Test
    public void throws_dodoexception_with_i_o_error_as_cause_if_reading_causes_problems() throws Exception {
        streamReadOpAnswer(invocation -> {
            throw EXPECTED_IO_EXCEPTION;
        });
        DodoException expectedException = new DodoException("Encountered error while reading from stream.", EXPECTED_IO_EXCEPTION);

        ExpectedExceptions.assertThatThrownBy(() -> underTest.read(), expectedException);
    }

    @Test
    public void constructing_with_block_size_zero_raises_illegal_argument() {
        assertThatThrownBy(() -> new StreamDataSource(streamMock, 0)).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Block size must be greater than zero.");
    }

    @Test
    public void constructing_with_negative_block_size_raises_illegal_argument() {
        assertThatThrownBy(() -> new StreamDataSource(streamMock, -1)).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Block size must be greater than zero.");
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

    private void constructUnderTest(int dataLengthBytes) throws Exception {
        realStream = new ByteArrayInputStream(DATA);

        streamReadOpAnswer(invocation -> {
            byte[] buf = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            return realStream.readNBytes(buf, offset, len);
        });
        underTest = new StreamDataSource(streamMock, dataLengthBytes);
    }
}
