package de.itemis.jmo.dodo.io;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.EXPECTED_IO_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.tests.util.ExpectedExceptions;

public class HttpDownloadTest {

    private UrlWrapper urlMock;
    private InputStream streamMock;

    private HttpDownload underTest;

    @BeforeEach
    public void setUp() throws Exception {
        urlMock = mock(UrlWrapper.class);
        streamMock = mock(InputStream.class);
        when(urlMock.openStream()).thenReturn(streamMock);

        underTest = new HttpDownload(urlMock);
    }

    @Test
    public void get_data_source_returns_a_data_source() {
        StreamDataSource result = underTest.getDataSource();

        assertThat(result).isNotNull();
    }

    @Test
    public void get_data_source_uses_url_to_open_stream() throws Exception {
        underTest.getDataSource();

        verify(urlMock).openStream();
    }

    @Test
    public void io_exceptions_during_stream_opening_are_propagated_as_dodo_exceptions() throws Exception {
        when(urlMock.openStream()).thenThrow(EXPECTED_IO_EXCEPTION);
        DodoException expectedException = new DodoException("Encountered error while opening a stream.", EXPECTED_IO_EXCEPTION);

        ExpectedExceptions.assertThatThrownBy(() -> underTest.getDataSource(), expectedException);
    }
}
