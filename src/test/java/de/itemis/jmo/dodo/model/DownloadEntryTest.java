package de.itemis.jmo.dodo.model;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.DODO_EXCEPTION;
import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.EXPECTED_IO_EXCEPTION;
import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.itemis.jmo.dodo.error.DodoWarning;
import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.io.Persistence;
import de.itemis.jmo.dodo.io.ProgressListener;

@RunWith(JUnitPlatform.class)
public class DownloadEntryTest {

    private static final String ARTIFACT_NAME = "artifactName";
    private static final long ARTIFACT_SIZE = 100;
    private static final Path FAKE_PATH = Paths.get(DownloadEntryTest.class.getSimpleName() + "FakePath");

    private DownloadScript downloadScriptMock;
    private Persistence persistenceMock;

    private DodoDownload downloadMock;
    private DataSource dataSourceMock;

    private DownloadEntry underTest;

    @BeforeEach
    public void setUp() {
        downloadScriptMock = mock(DownloadScript.class);
        persistenceMock = mock(Persistence.class);
        downloadMock = mock(DodoDownload.class);
        dataSourceMock = mock(DataSource.class);

        when(downloadScriptMock.createDownload()).thenReturn(downloadMock);
        when(downloadMock.getDataSource()).thenReturn(dataSourceMock);
        when(downloadMock.getSize()).thenReturn(ARTIFACT_SIZE);

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
    public void download_passesDataSource_to_persistence() {
        download();

        verify(persistenceMock).write(same(dataSourceMock), same(FAKE_PATH), anyProgressListener());
    }

    @Test
    public void persistence_dodoexceptions_are_propagated_as_is() {
        doThrow(DODO_EXCEPTION).when(persistenceMock).write(same(dataSourceMock), same(FAKE_PATH), anyProgressListener());

        assertThatThrownBy(downloadOp()).isSameAs(DODO_EXCEPTION);
    }

    @Test
    public void persistence_dodoexceptions_cause_source_to_be_closed() throws Exception {
        doThrow(DODO_EXCEPTION).when(persistenceMock).write(same(dataSourceMock), same(FAKE_PATH), anyProgressListener());

        assertThatThrownBy(downloadOp());

        verify(dataSourceMock).close();
    }

    @Test
    public void exceptions_during_source_close_become_warnings() throws Exception {
        DodoWarning expectedWarning = new DodoWarning("Could not close data source.", EXPECTED_IO_EXCEPTION);
        doThrow(EXPECTED_IO_EXCEPTION).when(dataSourceMock).close();

        assertThatThrownBy(downloadOp(), expectedWarning);
    }

    @Test
    public void complete_persistence_write_completes_download_progress() {
        // Mockito's mock API doesn't go well with generics.
        @SuppressWarnings("unchecked")
        ProgressListener<Double> listenerMock = mock(ProgressListener.class);
        doAnswer(invocation -> {
            ProgressListener<Long> persistenceListener = invocation.getArgument(2);
            persistenceListener.updateProgress(ARTIFACT_SIZE);
            return null;
        }).when(persistenceMock).write(same(dataSourceMock), same(FAKE_PATH), anyProgressListener());
        underTest.addDownloadListener(listenerMock);

        download();

        verify(listenerMock).updateProgress(eq(100.0));
    }

    @Test
    public void write_steps_cause_an_appropriate_progress_update_on_listeners() {
        // Mockito's mock API doesn't go well with generics.
        @SuppressWarnings("unchecked")
        ProgressListener<Double> listenerMock = mock(ProgressListener.class);
        int stepCount = 4;
        doAnswer(invocation -> {
            ProgressListener<Long> persistenceListener = invocation.getArgument(2);
            for (var i = 1; i <= stepCount; i++) {
                persistenceListener.updateProgress((ARTIFACT_SIZE / stepCount) * i);
            }
            return null;
        }).when(persistenceMock).write(same(dataSourceMock), same(FAKE_PATH), anyProgressListener());
        underTest.addDownloadListener(listenerMock);

        download();

        InOrder order = Mockito.inOrder(listenerMock);
        order.verify(listenerMock).updateProgress(eq(25.0));
        order.verify(listenerMock).updateProgress(eq(50.0));
        order.verify(listenerMock).updateProgress(eq(75.0));
        order.verify(listenerMock).updateProgress(eq(100.0));
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

    // Mockito's any API doesn't go well with generics, but the "cast" is safe, because generics are
    // removed at runtime anyway and we know that types are compatible.
    @SuppressWarnings("unchecked")
    private ProgressListener<Long> anyProgressListener() {
        return any(ProgressListener.class);
    }
}
