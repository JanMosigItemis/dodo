package de.itemis.jmo.dodo.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.net.URI;

import de.itemis.jmo.dodo.tests.testfx.DodoUiTestDriver;
import de.itemis.jmo.dodo.tests.testfx.JavaFxDodoTestDriver;
import de.itemis.jmo.dodo.tests.util.FakeServer;

@RunWith(JUnitPlatform.class)
public class DodoUiTest {

    private static final String ARTIFACT_NAME = "test.artifact.one";

    private static FakeServer fakeServer;

    private DodoUiTestDriver dodo;

    @BeforeAll
    public static void setUpStatic() {
        fakeServer = new FakeServer();
    }

    @BeforeEach
    public void setUp() {
        dodo = new JavaFxDodoTestDriver();
        dodo.beforeEach();
    }

    @AfterEach
    public void tearDown() {
        if (dodo != null) {
            dodo.afterEach();
        }
        fakeServer.stop();
        dodo = null;
    }

    @Test
    public void when_noSource_nothing_is_displayed() {
        dodo.assertNoDownloadEntriesDisplayed();
    }

    @Test
    public void when_downloadSource_hasBeenAdded_itGetsDisplayed() {
        dodo.addDownloadSource(ARTIFACT_NAME, "someScript");
        dodo.assertDownloadEntryDisplayed(ARTIFACT_NAME);
    }

    @Test
    public void when_delete_then_entry_is_removed() {
        dodo.addDownloadSource(ARTIFACT_NAME, "someScript");
        dodo.delete(ARTIFACT_NAME);
        dodo.assertNoDownloadEntriesDisplayed();
    }

    @Test
    public void when_downloading_artifact_indicate_success() {
        URI artifactUri = fakeServer.provide(ARTIFACT_NAME);
        dodo.addDownloadSource(ARTIFACT_NAME, artifactUri.toString());
        dodo.download(ARTIFACT_NAME);
        dodo.assertDownloadSuccessIndicated(ARTIFACT_NAME);
    }
}
