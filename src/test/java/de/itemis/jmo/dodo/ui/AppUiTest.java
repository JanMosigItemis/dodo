package de.itemis.jmo.dodo.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.net.URI;

import de.itemis.jmo.dodo.tests.DodoTestRunner;
import de.itemis.jmo.dodo.tests.FakeServer;
import de.itemis.jmo.dodo.tests.JavaFxDodoTestRunner;

@RunWith(JUnitPlatform.class)
public class AppUiTest {

    private static final String ARTIFACT_NAME = "test.artifact.one";

    private static FakeServer fakeServer;

    private DodoTestRunner dodoTestRunner;

    @BeforeAll
    public static void setUpStatic() {
        fakeServer = new FakeServer();
    }

    @BeforeEach
    public void setUp() {
        dodoTestRunner = new JavaFxDodoTestRunner();
        dodoTestRunner.beforeEach();
    }

    @AfterEach
    public void tearDown() {
        if (dodoTestRunner != null) {
            dodoTestRunner.afterEach();
        }
        fakeServer.stop();
        dodoTestRunner = null;
    }

    @Test
    public void when_downloading_existingArtifact_indicate_success() {
        URI artifactUri = fakeServer.provide(ARTIFACT_NAME);
        dodoTestRunner.addDownloadSource(ARTIFACT_NAME, artifactUri);
        dodoTestRunner.download(ARTIFACT_NAME);
        dodoTestRunner.assertDownloadSuccessIndicated(ARTIFACT_NAME);
    }
}
