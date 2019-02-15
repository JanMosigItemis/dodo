package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import java.net.URI;

import nl.jqno.equalsverifier.EqualsVerifier;

public class DownloadScriptTest {

    private static final URI ARTIFACT_URI = URI.create("artifactUri");

    // Will be used later on
    @SuppressWarnings("unused")
    private DownloadScript underTest;

    @Test
    public void setUp() {
        underTest = new DownloadScript(ARTIFACT_URI);
    }

    @Test
    public void constructor_doesNotAcceptNullUri() {
        assertThatThrownBy(() -> new DownloadScript(null)).isExactlyInstanceOf(NullPointerException.class).hasMessageContaining("artifactUri");
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(DownloadScript.class).usingGetClass().verify();
    }
}
