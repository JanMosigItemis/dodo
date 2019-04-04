package de.itemis.jmo.dodo.ui;

import static de.itemis.jmo.dodo.tests.util.TestHelper.getLocalizedDecimalSeparator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.nio.file.Path;

import de.itemis.jmo.dodo.tests.testfx.DodoUiTestDriver;
import de.itemis.jmo.dodo.tests.testfx.JavaFxDodoTestDriver;

@RunWith(JUnitPlatform.class)
public class DodoUiTest {

    private static final String ARTIFACT_ONE_NAME = "test.artifact.one";

    /*
     * Managed in a static way, because it is not possible to "restart" a JavaFX app during one test
     * run.
     */
    private static final DodoUiTestDriver DODO;

    static {
        OpenJfxMonocleWindowsBugWorkaround.runIfOnWindows();
        DODO = new JavaFxDodoTestDriver();
    }

    @BeforeAll
    public static void setUpStatic() {
        DODO.beforeAll();
    }

    @AfterAll
    public static void tearDownStatic() {
        DODO.afterAll();
    }

    @BeforeEach
    public void setUp(@TempDir Path tmpDir) {
        DODO.initPersistence(tmpDir);
        DODO.beforeEach();
    }

    @AfterEach
    public void tearDown(@TempDir Path tmpDir) {
        DODO.afterEach();
    }

    @Test
    public void when_noSource_nothing_is_displayed() {
        DODO.assertNoDownloadEntriesDisplayed();
    }

    @Test
    public void when_downloadSource_hasBeenAdded_itGetsDisplayed() {
        DODO.addDownloadSource(ARTIFACT_ONE_NAME);
        DODO.assertDownloadEntryDisplayed(ARTIFACT_ONE_NAME);
    }

    @Test
    public void when_delete_then_entry_is_removed() {
        DODO.addDownloadSource(ARTIFACT_ONE_NAME);
        DODO.delete(ARTIFACT_ONE_NAME);
        DODO.assertNoDownloadEntriesDisplayed();
    }

    @Test
    public void when_download_is_successful_indicate_success() {
        DODO.addDownloadSource(ARTIFACT_ONE_NAME);
        DODO.initiateDownload(ARTIFACT_ONE_NAME);
        DODO.waitUntilDownloadFinished(ARTIFACT_ONE_NAME);
        DODO.assertDownloadSuccessIndicated(ARTIFACT_ONE_NAME);
    }

    @Test
    public void when_download_is_successful_then_artifact_is_stored() {
        DODO.addDownloadSource(ARTIFACT_ONE_NAME);
        DODO.initiateDownload(ARTIFACT_ONE_NAME);
        DODO.waitUntilDownloadFinished(ARTIFACT_ONE_NAME);

        DODO.assertDownloadStored(ARTIFACT_ONE_NAME);
    }

    // Be aware of the different formats for floating point nbrs. depending on the default locale.
    @Test
    public void when_download_is_active_show_progress() {
        int stallPercentage = 50;
        String decimalSeparator = getLocalizedDecimalSeparator();
        String expectedText = stallPercentage + decimalSeparator + "0";
        DODO.addDownloadSource(ARTIFACT_ONE_NAME);
        DODO.letDownloadStallAt(ARTIFACT_ONE_NAME, stallPercentage);
        DODO.initiateDownload(ARTIFACT_ONE_NAME);

        DODO.assertDownloadProgressBarAt(ARTIFACT_ONE_NAME, stallPercentage);
        DODO.assertDownloadProgressText(ARTIFACT_ONE_NAME, expectedText);
    }
}
