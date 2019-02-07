/**
 *
 */
package de.itemis.jmo.dodo.tests;

import static de.itemis.jmo.dodo.tests.TestHelper.fail;
import static de.itemis.jmo.dodo.tests.TestHelper.printWarning;
import static de.itemis.jmo.dodo.util.NodeIdSanitizer.sanitize;
import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import org.testfx.framework.junit5.ApplicationTest;

import java.net.URI;

import de.itemis.jmo.dodo.DodoApp;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * A "driver" for UI tests. It decouples the actual test code ("what to test") from the knowledge on
 * how to actually perform those tests. This helps keeping the tests compiling in case test
 * technology or app technology changes.
 */
public class JavaFxDodoTestRunner extends ApplicationTest implements DodoTestRunner {

    private DodoApp dodo;

    @Override
    public void beforeEach() {
        try {
            internalBefore();
            waitForFxEvents();
        } catch (Exception e) {
            fail("Could not init test runner.", e);
        }
    }

    @Override
    public void afterEach() {
        try {
            waitForFxEvents();
            internalAfter();
        } catch (Exception e) {
            printWarning("Test runner cleanup failed.", e);
        }
    }

    @Override
    public void start(Stage testStage) throws Exception {
        dodo = new DodoApp();
        dodo.start(testStage);
    }

    @Override
    public void stop() throws Exception {
        dodo.stop();
    }

    @Override
    public void addDownloadSource(String artifactName, URI artifactUri) {
        clickOn("#dodoMenu");
        clickOn("#addSource");
        lookup("#addSource_artifactName").queryTextInputControl().setText(artifactName);
        lookup("#addSource_artifactUri").queryTextInputControl().setText(artifactUri.toString());
        clickOn("#addSource_confirm");
        waitForFxEvents();
    }

    @Override
    public void download(String artifactName) {
        String sanitizedArtifactName = sanitize(artifactName);
        clickOn("#downloadButton_" + sanitizedArtifactName);
        waitForFxEvents();
    }

    @Override
    public void assertDownloadSuccessIndicated(String artifactName) {
        assertThat(itemTable()).hasTableCell(true);
    }

    @Override
    public void assertNoDownloadEntriesDisplayed() {
        assertThat(itemTable()).hasExactlyNumRows(0);
    }

    @Override
    public void assertDownloadEntryDisplayed(String artifactName) {
        assertThat(itemTable()).containsRow(artifactName, false);
    }

    private TableView<Object> itemTable() {
        return lookup("#itemTable").queryTableView();
    }
}
