package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class DownloadEntryTest {

    private static final String DOWNLOAD_SCRIPT = "downloadScript";
    private static final String ARTIFACT_NAME = "artifactName";

    private DownloadEntry underTest;

    @BeforeEach
    public void setUp() {
        underTest = new DownloadEntry(ARTIFACT_NAME, DOWNLOAD_SCRIPT);
    }

    @Test
    public void constructorDoesNotAcceptNullName() {
        assertThrows(NullPointerException.class, () -> new DownloadEntry(null, DOWNLOAD_SCRIPT));
    }

    @Test
    public void constructorDoesNotAcceptNullScript() {
        assertThrows(NullPointerException.class, () -> new DownloadEntry(ARTIFACT_NAME, null));
    }

    @Test
    public void getArtifactNameReturnsName() {
        assertThat(underTest.getArtifactName()).isEqualTo(ARTIFACT_NAME);
    }

    @Test
    public void getDownloadScriptReturnsScript() {
        assertThat(underTest.getDownloadScript()).isEqualTo(DOWNLOAD_SCRIPT);
    }

    @Test
    public void downloadFlag_isFalse_byDefault() {
        assertThat(underTest.isDownloadFinished()).isFalse();
    }

    @Test
    public void successful_download_sets_downloadFlag_toTrue() {
        underTest.download();
        assertThat(underTest.isDownloadFinished()).isTrue();
    }

}
