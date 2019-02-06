package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.net.URI;

@RunWith(JUnitPlatform.class)
public class DownloadEntryTest {

    private static final URI ARTIFACT_URI = URI.create("testUri");
    private static final String ARTIFACT_NAME = "artifactName";

    private DownloadEntry underTest;

    @BeforeEach
    public void setUp() {
        underTest = new DownloadEntry(ARTIFACT_NAME, ARTIFACT_URI);
    }

    @Test
    public void constructorDoesNotAcceptNullName() {
        assertThrows(NullPointerException.class, () -> new DownloadEntry(null, ARTIFACT_URI));
    }

    @Test
    public void constructorDoesNotAcceptNullUri() {
        assertThrows(NullPointerException.class, () -> new DownloadEntry(ARTIFACT_NAME, null));
    }

    @Test
    public void getArtifactNameReturnsName() {
        assertThat(underTest.getArtifactName()).isEqualTo(ARTIFACT_NAME);
    }

    @Test
    public void getArtifactUriReturnsUri() {
        assertThat(underTest.getArtifactUri()).isEqualTo(ARTIFACT_URI);
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
