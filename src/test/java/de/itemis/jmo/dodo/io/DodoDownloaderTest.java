package de.itemis.jmo.dodo.io;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URI;

import de.itemis.jmo.dodo.tests.util.FakeServer;

public class DodoDownloaderTest {

    /*
     * Managed in a static way in order to safe test startup / tear down time. By doing it static,
     * the server has to be started / stopped only once. Downside: Test isolation is a bit weaker by
     * reusing the server in between tests.
     */
    private static final FakeServer FAKE_SERVER = new FakeServer();

    private DodoDownloader underTest;

    @BeforeAll
    public static void tearDownStatic() {
        FAKE_SERVER.stop();
    }

    @BeforeEach
    public void setUp() {
        underTest = new DodoDownloader();
    }

    @Test
    public void openStream_returns_a_stream() throws Exception {
        URI artifactUri = FAKE_SERVER.provide("test.artifact.one");
        try (InputStream stream = underTest.openStream(artifactUri)) {
            assertThat(stream).isNotNull();
        }
    }
}
