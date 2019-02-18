package de.itemis.jmo.dodo;

import java.nio.file.Path;
import java.util.Optional;

import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Implementations of this interface are capable of displaying OS dependent dialogs, e. g. file
 * chooser etc.
 */
public interface DodoSystemDialogs {

    /**
     * Like {@link FileChooser#showSaveDialog(javafx.stage.Window)} but returns a more convenient
     * {@link Path} instead.
     *
     * @param ownerWindow - the owner window of the displayed file dialog.
     * @return The selected path or {@link Optional#empty()} if none was selected.
     */
    Optional<Path> showSaveDialog(Window ownerWindow);
}
