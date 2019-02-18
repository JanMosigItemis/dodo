package de.itemis.jmo.dodo.tests.testfx;

import static de.itemis.jmo.dodo.tests.util.FutureHelper.assertLatch;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import de.itemis.jmo.dodo.AddDownloadSourceDialog;
import de.itemis.jmo.dodo.io.Persistence;
import de.itemis.jmo.dodo.model.DownloadEntry;
import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.parsing.StringParser;
import de.itemis.jmo.dodo.tests.util.FakeDownloader;
import de.itemis.jmo.dodo.tests.util.FakeStringToDownloadScriptParser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 * JavaFX based implementation of {@link AddDownloadSourceDialogUiTestDriver}.
 */
public class JavaFxAddDownloadSourceDialogUiTestDriver extends JavaFxBasedTestDriver
        implements AddDownloadSourceDialogUiTestDriver, ChangeListener<DownloadEntry> {

    private static final String INVALID_SCRIPT = "invalidScript";

    private StringParser<DownloadScript> parser;
    private Persistence persistence;
    private AtomicReference<Optional<DownloadEntry>> resultRef;
    private CountDownLatch resultReadyLatch;

    private Dialog<DownloadEntry> dialog;

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        parser = new FakeStringToDownloadScriptParser(INVALID_SCRIPT, new FakeDownloader());
        resultRef = new AtomicReference<>(Optional.empty());
        resultReadyLatch = new CountDownLatch(1);

        dialog = new AddDownloadSourceDialog(parser, persistence);
        dialog.setOnCloseRequest(event -> {
            resultReadyLatch.countDown();
        });
        dialog.resultProperty().addListener(this);
        dialog.show();
    }

    @Override
    public void stop() throws Exception {
        if (dialog != null && dialog.isShowing()) {
            clickCancelBtn();
        }

        resultReadyLatch = null;
        resultRef = null;
        parser = null;
        super.stop();
    }

    @Override
    public void changed(ObservableValue<? extends DownloadEntry> observable, DownloadEntry oldValue, DownloadEntry newValue) {
        resultRef.set(Optional.ofNullable(newValue));
        resultReadyLatch.countDown();
    }

    @Override
    public void enterArtifactName(String artifactName) {
        lookup("#addSource_artifactName").queryTextInputControl().setText(artifactName);
    }

    @Override
    public void enterDownloadScript(String downloadScript) {
        lookup("#addSource_downloadScript").queryTextInputControl().setText(downloadScript);
    }

    @Override
    public void enterInvalidDownloadScript() {
        enterDownloadScript(INVALID_SCRIPT);
    }

    @Override
    public void clickOkBtn() {
        clickOn("#addSource_confirm");
    }

    @Override
    public void clickCancelBtn() {
        clickOn("#addSource_cancel");
    }

    @Override
    public void assertOkBtnDisabled() {
        assertThat(lookup("#addSource_confirm").queryButton()).isDisabled();
    }

    @Override
    public void assertScriptHintLabelVisible() {
        assertThat(lookup("#addSource_downloadScript_hint").queryText()).isVisible();
    }

    @Override
    public Optional<DownloadEntry> waitOnResult(Duration timeout) {
        assertLatch(resultReadyLatch, timeout);
        return resultRef.get();
    }
}
