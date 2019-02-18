/**
 *
 */
package de.itemis.jmo.dodo.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import de.itemis.jmo.dodo.DodoSystemDialogs;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Uses dialogs provided by the OS's UI API. Probably not testable via TestFX.
 */
public class NativeOsDialogs implements DodoSystemDialogs {

    @Override
    public Optional<Path> showSaveDialog(Window ownerWindow) {
        Optional<Path> result = Optional.empty();

        var fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        var selectedFile = fileChooser.showSaveDialog(ownerWindow);

        if (selectedFile != null) {
            result = Optional.of(Paths.get(selectedFile.toURI()));
        }

        return result;
    }
}
