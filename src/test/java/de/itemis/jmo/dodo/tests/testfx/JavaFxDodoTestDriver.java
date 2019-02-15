/**
 *
 */
package de.itemis.jmo.dodo.tests.testfx;

import static de.itemis.jmo.dodo.util.NodeIdSanitizer.sanitize;
import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import de.itemis.jmo.dodo.DodoApp;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * JavaFX based implementation of a {@link DodoUiTestDriver}.
 */
public class JavaFxDodoTestDriver extends JavaFxBasedTestDriver implements DodoUiTestDriver {

    private DodoApp dodo;

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
    public void addDownloadSource(String artifactName, String downloadScript) {
        clickOn("#dodoMenu");
        clickOn("#addSource");
        lookup("#addSource_artifactName").queryTextInputControl().setText(artifactName);
        lookup("#addSource_downloadScript").queryTextInputControl().setText(downloadScript);
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
    public void delete(String artifactName) {
        String sanitizedArtifactName = sanitize(artifactName);
        clickOn("#deleteButton_" + sanitizedArtifactName);
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
