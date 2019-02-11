package de.itemis.jmo.dodo;

import java.net.URI;

import de.itemis.jmo.dodo.model.DownloadEntry;
import de.itemis.jmo.dodo.model.PropertyBiBinding;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * A dialog that collects data from the user and returns with an appropriate {@link DownloadEntry}.
 * Returns nothing on cancel.
 */
public class AddDownloadSourceDialog extends Dialog<DownloadEntry> {

    /**
     * Construct a new instance and requests focus.
     */
    public AddDownloadSourceDialog() {
        setTitle("Add Source");
        setHeaderText("Please enter the new source's data.");
        setResizable(true);

        Label nameLabel = new Label("Name: ");
        Label uriLabel = new Label("URI: ");
        TextField nameField = new TextField();
        nameField.setId("addSource_artifactName");

        TextField uriField = new TextField();
        uriField.setId("addSource_artifactUri");

        GridPane grid = new GridPane();
        grid.add(nameLabel, 1, 1);
        grid.add(nameField, 2, 1);
        grid.add(uriLabel, 1, 2);
        grid.add(uriField, 2, 2);
        getDialogPane().setContent(grid);

        ButtonType cancelBtnType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().add(cancelBtnType);
        Node cancelBtn = getDialogPane().lookupButton(cancelBtnType);
        cancelBtn.setId("addSource_cancel");

        ButtonType okBtnType = new ButtonType("OK", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(okBtnType);
        Node okBtn = getDialogPane().lookupButton(okBtnType);
        okBtn.setId("addSource_confirm");

        setResultConverter(clickedBtn -> {
            DownloadEntry result = null;
            if (clickedBtn == okBtnType) {
                result = new DownloadEntry(nameField.getText(), URI.create(uriField.getText()));
            }
            return result;
        });

        okBtn.disableProperty().bind(new PropertyBiBinding(nameField.textProperty(), uriField.textProperty()));

        nameField.requestFocus();
    }
}
