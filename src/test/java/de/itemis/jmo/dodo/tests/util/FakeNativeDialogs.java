package de.itemis.jmo.dodo.tests.util;

import java.nio.file.Path;
import java.util.Optional;

import de.itemis.jmo.dodo.DodoSystemDialogs;
import javafx.stage.Window;

/**
 * "Replaces" native dialogs with test dialogs (or at least a supplier for the expected result
 * value). Purpose of this class is to circumvent displaying native OS dialogs (e. g. FileChooser)
 * during UI testing. Since those are no JavaFX dialogs, they cannot be controlled by TestFX.
 */
public class FakeNativeDialogs implements DodoSystemDialogs {

    private Optional<Path> nextResultOfShowSaveDialog = Optional.empty();

    @Override
    public Optional<Path> showSaveDialog(Window ownerWindow) {
        return nextResultOfShowSaveDialog;
    }

    /**
     * Set the result of the next invocation of {@link #showSaveDialog(Window)} to the provided
     * {@code targetPath}.
     */
    public void nextResultOfShowSaveDialog(Optional<Path> targetPath) {
        nextResultOfShowSaveDialog = targetPath;
    }
}
