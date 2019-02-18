package de.itemis.jmo.dodo.ui;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import de.itemis.jmo.dodo.model.DownloadEntry;
import de.itemis.jmo.dodo.tests.testfx.AddDownloadSourceDialogUiTestDriver;
import de.itemis.jmo.dodo.tests.testfx.JavaFxAddDownloadSourceDialogUiTestDriver;

public class AddDownloadSourceDialogUiTest {

    private static final String VALID_SCRIPT = "validScript";
    private static final String ARTIFACT_NAME = "artifactName";
    private static final Duration DIALOG_CLOSE_TIMEOUT = Duration.ofSeconds(5);

    private AddDownloadSourceDialogUiTestDriver dialog;

    @BeforeEach
    public void setUp() {
        dialog = new JavaFxAddDownloadSourceDialogUiTestDriver();
        dialog.beforeEach();
    }

    @AfterEach
    public void tearDown() {
        if (dialog != null) {
            dialog.afterEach();
        }
        dialog = null;
    }

    @Test
    public void produces_newEntry_on_all_correct_data() {
        dialog.enterArtifactName(ARTIFACT_NAME);
        dialog.enterDownloadScript(VALID_SCRIPT);
        dialog.clickOkBtn();
        Optional<DownloadEntry> result = dialog.waitOnResult(DIALOG_CLOSE_TIMEOUT);

        assertThat(result).isPresent();
    }

    @Test
    public void produces_noEntry_on_Cancel() {
        dialog.enterArtifactName(ARTIFACT_NAME);
        dialog.enterDownloadScript(VALID_SCRIPT);
        dialog.clickCancelBtn();
        Optional<DownloadEntry> result = dialog.waitOnResult(DIALOG_CLOSE_TIMEOUT);

        assertThat(result).isNotPresent();
    }

    @Test
    public void okBtn_isDisabled_if_artifactName_empty() {
        dialog.enterArtifactName("   ");
        dialog.enterDownloadScript(VALID_SCRIPT);
        dialog.assertOkBtnDisabled();
    }

    @Test
    public void okBtn_isDisabled_if_downloadScript_invalid() {
        dialog.enterArtifactName(ARTIFACT_NAME);
        dialog.enterInvalidDownloadScript();
        dialog.assertOkBtnDisabled();
    }

    @Test
    public void invalidScript_displays_hintLabel() {
        dialog.enterInvalidDownloadScript();
        dialog.assertScriptHintLabelVisible();
    }
}
