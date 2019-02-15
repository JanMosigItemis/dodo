package de.itemis.jmo.dodo.tests.testfx;

import static de.itemis.jmo.dodo.tests.util.FutureHelper.assertLatch;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import de.itemis.jmo.dodo.AddDownloadSourceDialog;
import de.itemis.jmo.dodo.model.DownloadEntry;
import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.parsing.StringParser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 * JavaFX based implementation of {@link AddDownloadSourceDialogUiTestDriver}.
 */
public class JavaFxAddDownloadSourceDialogUiTestDriver extends JavaFxBasedTestDriver
        implements AddDownloadSourceDialogUiTestDriver, ChangeListener<DownloadEntry> {

    private final StringParser<DownloadScript> downloadScriptParser;

    private Dialog<DownloadEntry> dialog;
    private AtomicReference<Optional<DownloadEntry>> resultRef = new AtomicReference<>(Optional.empty());
    private CountDownLatch resultReadyLatch = new CountDownLatch(1);

    /**
     * Construct a new instance.
     *
     * @param downloadScriptParser - Use this to parse string input to a {@link DownloadScript}
     *        instance. Could be mocked / fake.
     */
    public JavaFxAddDownloadSourceDialogUiTestDriver(StringParser<DownloadScript> downloadScriptParser) {
        this.downloadScriptParser = downloadScriptParser;
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        dialog = new AddDownloadSourceDialog(downloadScriptParser);
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
