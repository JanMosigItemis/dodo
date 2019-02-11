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

    private static final String INVALID_URI = "-:invalid##/uri";
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
        String artifactName = "artifactName";
        String artifactUri = "artifactUri";

        dialog.enterArtifactName(artifactName);
        dialog.enterArtifactUri(artifactUri);
        dialog.clickOkBtn();
        Optional<DownloadEntry> result = dialog.waitOnResult(DIALOG_CLOSE_TIMEOUT);

        assertThat(result).isPresent();
        DownloadEntry resultValue = result.get();
        assertThat(resultValue.getArtifactName()).isEqualTo(artifactName);
        assertThat(resultValue.getArtifactUri()).hasToString(artifactUri);
    }

    @Test
    public void produces_noEntry_on_Cancel() {
        dialog.enterArtifactName("artifactName");
        dialog.enterArtifactUri("artifactUri");
        dialog.clickCancelBtn();
        Optional<DownloadEntry> result = dialog.waitOnResult(DIALOG_CLOSE_TIMEOUT);

        assertThat(result).isNotPresent();
    }

    @Test
    public void okBtn_isDisabled_if_artifactName_empty() {
        dialog.enterArtifactName("   ");
        dialog.enterArtifactUri("artifactUri");
        dialog.assertOkBtnDisabled();
    }

    @Test
    public void okBtn_isDisabled_if_artifactUri_empty() {
        dialog.enterArtifactName("artifactName");
        dialog.enterArtifactUri("   ");
        dialog.assertOkBtnDisabled();
    }

    @Test
    public void invalidUri_displays_hintLabel() {
        dialog.enterArtifactUri(INVALID_URI);
        dialog.assertUriHintLabelVisible();
    }

    @Test
    public void invalidUri_disables_okBtn() {
        dialog.enterArtifactName("artifactName");
        dialog.enterArtifactUri(INVALID_URI);
        dialog.assertOkBtnDisabled();
    }
}
