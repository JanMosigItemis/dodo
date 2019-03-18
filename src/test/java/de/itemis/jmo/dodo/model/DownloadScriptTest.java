package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;

public class DownloadScriptTest {

    private DodoDownload downloadMock;
    private Supplier<DodoDownload> downloadSup;

    private DownloadScript underTest;

    @BeforeEach
    public void setUp() {
        downloadMock = mock(DodoDownload.class);
        downloadSup = () -> downloadMock;

        underTest = new DownloadScript(downloadSup);
    }

    @Test
    public void createDownload_calls_download_create_function() {
        DodoDownload result = underTest.createDownload();

        assertThat(result).as("result of method").isEqualTo(downloadMock);
    }
}
