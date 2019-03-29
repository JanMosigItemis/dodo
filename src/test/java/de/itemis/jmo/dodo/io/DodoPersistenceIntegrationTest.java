package de.itemis.jmo.dodo.io;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.DODO_EXCEPTION;
import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.EXPECTED_IO_EXCEPTION;
import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.assertThatThrownBy;
import static de.itemis.jmo.dodo.util.Sneaky.throwThat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.size;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.model.DodoPersistence;

public class DodoPersistenceIntegrationTest {

    private static final byte[] END_OF_SOURCE = new byte[0];
    private static final String ARTIFACT_NAME = "artifactName";
    private static final String DOWNLOAD_STRING = "Hello World!";
    private static final byte[] DOWNLOAD_BYTES = DOWNLOAD_STRING.getBytes(UTF_8);

    @TempDir
    protected Path tmpDir;

    private DataSource dataSourceMock;
    private Path targetPath;

    private DodoPersistence underTest;

    @BeforeEach
    public void setUp() {
        dataSourceMock = mock(DataSource.class);
        when(dataSourceMock.read()).thenReturn(DOWNLOAD_BYTES, END_OF_SOURCE);

        targetPath = constructTargetPath(tmpDir);
        underTest = new DodoPersistence();
    }

    @Test
    public void if_file_does_not_exist_it_is_created() {
        assertThat(targetPath).doesNotExist();

        write();

        assertThat(targetPath).exists();
    }

    @Test
    public void if_file_does_exist_it_is_truncated() throws Exception {
        byte[] previousContents = new byte[] {1, 2, 3};
        Files.write(targetPath, previousContents);

        write();

        assertThat(targetPath).usingCharset(UTF_8).hasContent(DOWNLOAD_STRING);
    }

    @Test
    public void all_bytes_from_download_are_stored() throws Exception {
        write();

        assertThat(size(targetPath)).isEqualTo(DOWNLOAD_BYTES.length);
    }

    @Test
    public void dodoExceptions_onRead_arePassed_asIs() {
        doThrow(DODO_EXCEPTION).when(dataSourceMock).read();

        assertThatThrownBy(() -> write(), DODO_EXCEPTION);
    }

    /**
     * Can't simulate an exception on write, so we sneak one in during read.
     */
    @Test
    public void ioException_onWrite_causes_dodoException() {
        DodoException expectedException = new DodoException("Error while writing data to local filesystem.", EXPECTED_IO_EXCEPTION);
        doAnswer(invocation -> {
            throwThat(EXPECTED_IO_EXCEPTION);
            return null;
        }).when(dataSourceMock).read();

        assertThatThrownBy(() -> write(), expectedException);
    }

    @Test
    public void writes_all_data_buffer_size_is_less_than_file_size() throws Exception {
        when(dataSourceMock.read()).thenReturn(DOWNLOAD_BYTES, DOWNLOAD_BYTES, DOWNLOAD_BYTES, END_OF_SOURCE);

        write();

        assertThat(size(targetPath)).isEqualTo(3 * DOWNLOAD_BYTES.length);
    }

    @Test
    public void writes_all_data_buffer_size_is_equal_to_file_size() throws Exception {
        write();

        assertThat(size(targetPath)).isEqualTo(DOWNLOAD_BYTES.length);
    }

    @Test
    public void write_informs_listener() {
        // Mocking generic types with mockito cannot be done in a compiler safe way.
        @SuppressWarnings("unchecked")
        ProgressListener<Long> listener = mock(ProgressListener.class);
        dataSourceMock = new StreamDataSource(new ByteArrayInputStream(DOWNLOAD_BYTES), DOWNLOAD_BYTES.length / 4);
        underTest.write(dataSourceMock, targetPath, listener);

        InOrder order = Mockito.inOrder(listener);
        order.verify(listener).updateProgress(3L);
        order.verify(listener).updateProgress(6L);
        order.verify(listener).updateProgress(9L);
        order.verify(listener).updateProgress(12L);
    }

    /*
     * ##########################
     *
     * Start private helper code.
     *
     * ##########################
     */

    private void write() {
        underTest.write(dataSourceMock, targetPath);
    }

    private Path constructTargetPath(Path tmpDir) {
        return tmpDir.resolve(ARTIFACT_NAME);
    }
}
