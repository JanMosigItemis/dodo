package de.itemis.jmo.dodo.io;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.EXPECTED_IO_EXCEPTION;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.size;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.model.DodoPersistence;

public class DodoPersistenceIntegrationTest {

    private static final String ARTIFACT_NAME = "artifactName";
    private static final String DOWNLOAD_STRING = "Hello World!";
    private static final byte[] DOWNLOAD_BYTES = DOWNLOAD_STRING.getBytes(UTF_8);

    @TempDir
    protected Path tmpDir;

    private Path targetPath;

    private Persistence underTest;

    @BeforeEach
    public void setUp() {
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

        assertThat(size(targetPath)).isEqualTo(DOWNLOAD_BYTES.length);
        assertThat(targetPath).usingCharset(UTF_8).hasContent(DOWNLOAD_STRING);
    }

    @Test
    public void all_bytes_from_download_are_stored() throws Exception {
        write();

        assertThat(size(targetPath)).isEqualTo(DOWNLOAD_BYTES.length);
    }

    @Test
    public void on_read_error_dodoexception() throws Exception {
        InputStream faultyStream = mock(InputStream.class);
        doThrow(EXPECTED_IO_EXCEPTION).when(faultyStream).transferTo(Mockito.any(OutputStream.class));

        assertThatThrownBy(() -> underTest.write(targetPath, faultyStream))
            .isInstanceOf(DodoException.class)
            .hasMessage("Error while writing data to local filesystem.")
            .hasCause(EXPECTED_IO_EXCEPTION);
    }

    /*
     * ##########################
     *
     * Start private helper code.
     *
     * ##########################
     */

    private void write() {
        underTest.write(targetPath, createStream(DOWNLOAD_BYTES));
    }

    private InputStream createStream(byte[] downloadBytes) {
        return new ByteArrayInputStream(downloadBytes);
    }

    private Path constructTargetPath(Path tmpDir) {
        return tmpDir.resolve(ARTIFACT_NAME);
    }
}
