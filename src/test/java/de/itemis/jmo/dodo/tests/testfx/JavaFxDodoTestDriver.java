/**
 *
 */
package de.itemis.jmo.dodo.tests.testfx;

import static de.itemis.jmo.dodo.tests.util.JsonTestHelper.toDoubleQuotes;
import static de.itemis.jmo.dodo.tests.util.TestHelper.fail;
import static de.itemis.jmo.dodo.util.NodeIdSanitizer.sanitize;
import static java.nio.file.Files.size;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import de.itemis.jmo.dodo.DodoApp;
import de.itemis.jmo.dodo.io.DodoDownloader;
import de.itemis.jmo.dodo.io.Downloader;
import de.itemis.jmo.dodo.io.Persistence;
import de.itemis.jmo.dodo.model.DodoPersistence;
import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.parsing.JsonScriptParser;
import de.itemis.jmo.dodo.parsing.StringParser;
import de.itemis.jmo.dodo.tests.util.FakeNativeDialogs;
import de.itemis.jmo.dodo.tests.util.FakeServer;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * JavaFX based implementation of a {@link DodoUiTestDriver}.
 */
public class JavaFxDodoTestDriver extends JavaFxBasedTestDriver implements DodoUiTestDriver {

    /*
     * Managed in a static way in order to safe test startup / tear down time. By doing it static,
     * the server has to be started / stopped only once. Downside: Test isolation is a bit weaker by
     * reusing the server in between tests.
     */
    private static final FakeServer FAKE_SERVER = new FakeServer();

    private final FakeNativeDialogs fakeDialogs = new FakeNativeDialogs();
    private final Downloader downloader = new DodoDownloader();
    private final StringParser<DownloadScript> scriptParser = new JsonScriptParser(downloader);
    private final Persistence persistence = new DodoPersistence();

    private Path tmpDir;

    private DodoApp dodo;

    @Override
    public void afterAll() {
        FAKE_SERVER.stop();
        super.afterAll();
    }

    @Override
    public void initPersistence(Path tmpDirPath) {
        super.initPersistence(tmpDirPath);
        this.tmpDir = tmpDirPath;
    }

    @Override
    public void start(Stage testStage) throws Exception {
        dodo = new DodoApp(fakeDialogs, scriptParser, persistence);
        dodo.start(testStage);
    }

    @Override
    public void stop() throws Exception {
        dodo.stop();
    }

    @Override
    public void addDownloadSource(String artifactName) {
        String script = FAKE_SERVER.provide(artifactName).toString();
        script = toDoubleQuotes("{'uri':'" + script + "'}");
        clickOn("#dodoMenu");
        clickOn("#addSource");
        lookup("#addSource_artifactName").queryTextInputControl().setText(artifactName);
        lookup("#addSource_downloadScript").queryTextInputControl().setText(script);
        clickOn("#addSource_confirm");
        waitForFxEvents();
    }

    @Override
    public void download(String artifactName) {
        Path targetPath = constructTargetPath(artifactName);
        fakeDialogs.nextResultOfShowSaveDialog(Optional.of(targetPath));
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

    @Override
    public void assertDownloadStored(String artifactName) {
        Path targetPath = constructTargetPath(artifactName);
        long expectedArtifactSize = FAKE_SERVER.getSize(artifactName);
        long actualArtifactSize = 0;
        try {
            actualArtifactSize = size(targetPath);
        } catch (IOException e) {
            fail("It appears that the test artifact has not been downloaded successfully.", e);
        }

        assertThat(actualArtifactSize).isEqualTo(expectedArtifactSize);
    }

    private Path constructTargetPath(String artifactName) {
        if (tmpDir == null) {
            throw new IllegalStateException("Please call initPersistence before running any tests.");
        }
        return tmpDir.resolve(artifactName);
    }
}
