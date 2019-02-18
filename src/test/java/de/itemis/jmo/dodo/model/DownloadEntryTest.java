package de.itemis.jmo.dodo.model;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.DODO_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.itemis.jmo.dodo.error.DodoWarning;
import de.itemis.jmo.dodo.io.Persistence;

@RunWith(JUnitPlatform.class)
public class DownloadEntryTest {

    private static final String ARTIFACT_NAME = "artifactName";
    private static final Path FAKE_PATH = Paths.get(DownloadEntryTest.class.getSimpleName() + "FakePath");

    private DownloadScript downloadScriptMock;
    private Persistence persistenceMock;
    private InputStream downloadStreamMock;

    private DownloadEntry underTest;

    @BeforeEach
    public void setUp() {
        downloadScriptMock = mock(DownloadScript.class);
        persistenceMock = mock(Persistence.class);
        downloadStreamMock = mock(InputStream.class);
        when(downloadScriptMock.createDownload()).thenReturn(downloadStreamMock);
        underTest = new DownloadEntry(ARTIFACT_NAME, downloadScriptMock, persistenceMock);
    }

    @Test
    public void getArtifactNameReturnsName() {
        assertThat(underTest.getArtifactName()).isEqualTo(ARTIFACT_NAME);
    }

    @Test
    public void downloadFlag_isFalse_byDefault() {
        assertThat(underTest.isDownloadFinished()).isFalse();
    }

    @Test
    public void successful_download_sets_downloadFlag_toTrue() {
        download();
        assertThat(underTest.isDownloadFinished()).isTrue();
    }

    @Test
    public void download_writes_scriptStream_to_persistence() {
        download();

        verify(persistenceMock).write(FAKE_PATH, downloadStreamMock);
    }

    @Test
    public void download_stream_is_closed_after_successful_download() throws Exception {
        download();

        verify(downloadStreamMock).close();
    }

    @Test
    public void script_dodoexceptions_are_propagated_as_is() {
        when(downloadScriptMock.createDownload()).thenThrow(DODO_EXCEPTION);

        assertThatThrownBy(downloadOp()).isSameAs(DODO_EXCEPTION);
    }

    @Test
    public void persistence_dodoexceptions_are_propagated_as_is() {
        doThrow(DODO_EXCEPTION).when(persistenceMock).write(FAKE_PATH, downloadStreamMock);

        assertThatThrownBy(downloadOp()).isSameAs(DODO_EXCEPTION);
    }

    @Test
    public void persistence_dodoexception_causes_downloadStream_to_be_closed() throws Exception {
        doThrow(DODO_EXCEPTION).when(persistenceMock).write(FAKE_PATH, downloadStreamMock);

        assertThatThrownBy(downloadOp());

        verify(downloadStreamMock).close();
    }

    @Test
    public void exception_during_download_stream_close_raises_dodowarning() throws Exception {
        IOException expectedCause = new IOException();
        doThrow(expectedCause).when(downloadStreamMock).close();

        assertThatThrownBy(downloadOp()).isInstanceOf(DodoWarning.class).hasCause(expectedCause)
            .hasMessage("Error while closing stream.");
    }

    /*
     * ##########################
     *
     * Start private helper code.
     *
     * ##########################
     */

    private void download() {
        underTest.download(FAKE_PATH);
    }

    private ThrowingCallable downloadOp() {
        return () -> download();
    }
}
