package de.itemis.jmo.dodo.model;

import org.junit.jupiter.api.Test;

import java.net.URI;

import de.itemis.jmo.dodo.tests.util.FakeDownloader;
import nl.jqno.equalsverifier.EqualsVerifier;

public class DownloadScriptTest {

    private static final URI ARTIFACT_URI = URI.create("artifactUri");

    private FakeDownloader fakeDownloader;

    // Will be used later on
    @SuppressWarnings("unused")
    private DownloadScript underTest;

    @Test
    public void setUp() {
        fakeDownloader = new FakeDownloader();
        underTest = new DownloadScript(ARTIFACT_URI, fakeDownloader);
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(DownloadScript.class).usingGetClass().withIgnoredFields("downloader").verify();
    }
}
