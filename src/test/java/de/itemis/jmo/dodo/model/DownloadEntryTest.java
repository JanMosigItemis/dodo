package de.itemis.jmo.dodo.model;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.DODO_EXCEPTION;
import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.EXPECTED_IO_EXCEPTION;
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
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.io.DownloadResult;
import de.itemis.jmo.dodo.io.Persistence;
import de.itemis.jmo.dodo.io.ProgressListener;
import de.itemis.jmo.dodo.tests.util.Console;
import de.itemis.jmo.dodo.validation.ChecksumValidator;

@RunWith(JUnitPlatform.class)
public class DownloadEntryTest {

    private static final String ARTIFACT_NAME = "artifactName";
    private static final long ARTIFACT_SIZE = 100;
    private static final Path FAKE_PATH = Paths.get(DownloadEntryTest.class.getSimpleName() + "FakePath");

    @RegisterExtension
    Console console = new Console();

    private DownloadScript downloadScriptMock;
    private ChecksumValidator hashCodeValidatorMock;
    private Persistence persistenceMock;

    private DodoDownload downloadMock;
    private DataSource downloadDataSourceMock;
    private DataSource validationDataSourceMock;

    private DownloadEntry underTest;

    @BeforeEach
    public void setUp() {
        downloadScriptMock = mock(DownloadScript.class);
        hashCodeValidatorMock = mock(ChecksumValidator.class);
        persistenceMock = mock(Persistence.class);
        downloadMock = mock(DodoDownload.class);
        downloadDataSourceMock = mock(DataSource.class);
        validationDataSourceMock = mock(DataSource.class);

        when(persistenceMock.read(FAKE_PATH)).thenReturn(validationDataSourceMock);
        when(downloadScriptMock.createDownload()).thenReturn(downloadMock);
        when(downloadScriptMock.createHashCodeValidator()).thenReturn(hashCodeValidatorMock);
        when(downloadMock.getDataSource()).thenReturn(downloadDataSourceMock);
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
    public void download_passesDownloadDataSource_to_persistence() {
        download();

        verify(persistenceMock).write(same(downloadDataSourceMock), same(FAKE_PATH), anyProgressListener());
    }

    @Test
    public void persistence_dodoexceptions_are_propagated_as_is() {
        doThrow(DODO_EXCEPTION).when(persistenceMock).write(same(downloadDataSourceMock), same(FAKE_PATH), anyProgressListener());

        assertThatThrownBy(downloadOp()).isSameAs(DODO_EXCEPTION);
    }

    @Test
    public void persistence_dodoexceptions_cause_downloadSource_to_be_closed() throws Exception {
        doThrow(DODO_EXCEPTION).when(persistenceMock).write(same(downloadDataSourceMock), same(FAKE_PATH), anyProgressListener());

        assertThatThrownBy(downloadOp());

        verify(downloadDataSourceMock).close();
    }

    @Test
    public void exceptions_during_download_source_close_are_logged() throws Exception {
        doThrow(EXPECTED_IO_EXCEPTION).when(downloadDataSourceMock).close();

        download();

        console.anyLineContains("Encountered unexpected error while closing dataSource.");
        console.anyLineContains(EXPECTED_IO_EXCEPTION.getMessage());
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
        }).when(persistenceMock).write(same(downloadDataSourceMock), same(FAKE_PATH), anyProgressListener());
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
        }).when(persistenceMock).write(same(downloadDataSourceMock), same(FAKE_PATH), anyProgressListener());
        underTest.addDownloadListener(listenerMock);

        download();

        InOrder order = Mockito.inOrder(listenerMock);
        order.verify(listenerMock).updateProgress(eq(25.0));
        order.verify(listenerMock).updateProgress(eq(50.0));
        order.verify(listenerMock).updateProgress(eq(75.0));
        order.verify(listenerMock).updateProgress(eq(100.0));
    }

    @Test
    public void hashCode_validation_is_done_during_download() {
        download();

        verify(hashCodeValidatorMock).verify(validationDataSourceMock);
    }

    @Test
    public void download_returns_a_download_result() {
        var result = download();

        assertThat(result).isNotNull();
    }

    @Test
    public void when_hashCode_validation_fails_download_result_lists_this_error() {
        when(hashCodeValidatorMock.verify(downloadDataSourceMock)).thenReturn(false);

        var result = download();

        assertThat(result.hashIsValid()).as("unexpected hash code verification result").isFalse();
    }

    @Test
    public void when_validation_is_finished_data_source_is_closed() throws Exception {
        download();

        verify(validationDataSourceMock).close();
    }

    @Test
    public void dodoexceptions_during_read_are_propagated_as_is() {
        doThrow(DODO_EXCEPTION).when(persistenceMock).read(same(FAKE_PATH));

        assertThatThrownBy(downloadOp()).isSameAs(DODO_EXCEPTION);
    }

    @Test
    public void verify_dodoexceptions_cause_validation_source_to_be_closed() throws Exception {
        doThrow(DODO_EXCEPTION).when(hashCodeValidatorMock).verify(same(validationDataSourceMock));

        assertThatThrownBy(downloadOp());

        verify(validationDataSourceMock).close();
    }

    @Test
    public void error_during_validation_source_close_is_logged() throws Exception {
        doThrow(EXPECTED_IO_EXCEPTION).when(validationDataSourceMock).close();

        download();

        console.assertAnyLineContains("Encountered unexpected error while closing dataSource.");
        console.assertAnyLineContains(EXPECTED_IO_EXCEPTION.getMessage());
    }

    /*
     * ##########################
     *
     * Start private helper code.
     *
     * ##########################
     */

    private DownloadResult download() {
        return underTest.download(FAKE_PATH);
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
