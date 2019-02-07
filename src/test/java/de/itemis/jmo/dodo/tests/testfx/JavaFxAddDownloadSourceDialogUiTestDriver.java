package de.itemis.jmo.dodo.tests.testfx;

import static de.itemis.jmo.dodo.tests.util.FutureHelper.assertLatch;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import de.itemis.jmo.dodo.AddDownloadSourceDialog;
import de.itemis.jmo.dodo.model.DownloadEntry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

/**
 * JavaFX based implementation of {@link AddDownloadSourceDialogUiTestDriver}.
 */
public class JavaFxAddDownloadSourceDialogUiTestDriver extends JavaFxBasedTestDriver
        implements AddDownloadSourceDialogUiTestDriver, ChangeListener<DownloadEntry> {

    private Dialog<DownloadEntry> dialog;
    private AtomicReference<Optional<DownloadEntry>> resultRef = new AtomicReference<>(Optional.empty());
    private CountDownLatch resultReadyLatch = new CountDownLatch(1);

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        dialog = new AddDownloadSourceDialog();
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
    public void enterArtifactUri(String artifactUri) {
        lookup("#addSource_artifactUri").queryTextInputControl().setText(artifactUri.toString());
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
    public Optional<DownloadEntry> waitOnResult(Duration timeout) {
        assertLatch(resultReadyLatch, timeout);
        return resultRef.get();
    }
}
