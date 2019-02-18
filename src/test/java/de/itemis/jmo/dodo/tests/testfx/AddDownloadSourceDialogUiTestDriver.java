package de.itemis.jmo.dodo.tests.testfx;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import de.itemis.jmo.dodo.model.DownloadEntry;

/**
 * A "driver" for UI tests of AddDownloadSourceDialog. It decouples the actual test code ("what to
 * test") from the knowledge on how to actually perform those tests. This helps keeping the tests
 * compiling in case test technology or app technology changes.
 */
public interface AddDownloadSourceDialogUiTestDriver extends DodoTestsJunit5Lifecycle {

    /**
     * Enters the provided {@code artifactName} into the appropriate input field.
     */
    void enterArtifactName(String artifactName);

    /**
     * Enters the provided {@code downloadScript} into the appropriate input field.
     */
    void enterDownloadScript(String downloadScript);

    /**
     * Enter a download script that cannot be parsed successfully.
     */
    void enterInvalidDownloadScript();

    /**
     * Simulate a mouse click on the dialog's OK button.
     */
    void clickOkBtn();

    /**
     * Simulate a mouse click on the dialog's cancel button.
     */
    void clickCancelBtn();

    /**
     * @param timeout - Wait at max this long. Precision is milliseconds.
     * @return The result of the dialog or {@link Optional#empty()} if non (yet).
     * @throws TimeoutException in case waiting times out.
     */
    Optional<DownloadEntry> waitOnResult(Duration timeout);

    /**
     * Assert that the ok button is disabled.
     *
     * @throws AssertionError If the button is enabled.
     */
    void assertOkBtnDisabled();

    /**
     * Assert that the hint label for the download script text area is visible.
     */
    void assertScriptHintLabelVisible();
}
