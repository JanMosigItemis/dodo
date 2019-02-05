/**
 *
 */
package de.itemis.jmo.dodo.tests;

import static de.itemis.jmo.dodo.tests.TestHelper.fail;
import static de.itemis.jmo.dodo.tests.TestHelper.printWarning;
import static org.testfx.assertions.api.Assertions.assertThat;

import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.net.URI;

import de.itemis.jmo.dodo.DodoApp;
import de.itemis.jmo.dodo.model.DownloadEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

/**
 * A "driver" for UI tests. It decouples the actual test code ("what to test") from the knowledge on
 * how to actually perform those tests. This helps keeping the tests compiling in case test
 * technology or app technology changes.
 */
public class JavaFxDodoTestRunner extends ApplicationTest implements DodoTestRunner {

    private ObservableList<DownloadEntry> items;

    @Override
    public void beforeEach() {
        items = FXCollections.observableArrayList();
        try {
            internalBefore();
        } catch (Exception e) {
            fail("Could not init test runner.", e);
        }
    }

    @Override
    public void afterEach() {
        try {
            internalAfter();
            items.clear();
        } catch (Exception e) {
            printWarning("Test runner cleanup failed.", e);
        }
    }

    @Override
    public void start(Stage testStage) throws Exception {
        var dodo = new DodoApp(items);
        dodo.start(testStage);
    }

    @Override
    public void addDownloadSource(String artifactName, URI artifactUri) {
        DownloadEntry entry = new DownloadEntry(artifactName, artifactUri);
        items.add(entry);
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void download(String artifactName) {
        clickOn("#downloadButton");
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void assertDownloadSuccessIndicated(String artifactName) {
        assertThat(lookup("#itemTable").queryTableView()).hasTableCell(true);
    }
}
