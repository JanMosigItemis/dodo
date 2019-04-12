package de.itemis.jmo.dodo.io;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.EXPECTED_IO_EXCEPTION;
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

    private void constructUnderTest(int blockSizeBytes) throws Exception {
        realStream = new ByteArrayInputStream(DATA);

        streamReadOpAnswer(invocation -> {
            byte[] buf = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            return realStream.readNBytes(buf, offset, len);
        });

        underTest = new StreamDataSource(streamMock);
    }
}
