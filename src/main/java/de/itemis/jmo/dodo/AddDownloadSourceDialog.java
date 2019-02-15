package de.itemis.jmo.dodo;

import de.itemis.jmo.dodo.model.DownloadEntry;
import de.itemis.jmo.dodo.model.PropertyBiBinding;
import de.itemis.jmo.dodo.model.UriValidationBinding;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

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
        Label scriptLabel = new Label("Script: ");
        TextField nameField = new TextField();
        nameField.setId("addSource_artifactName");

        TextField scriptField = new TextField();
        scriptField.setId("addSource_downloadScript");

        Label blindLabel = new Label("blind");
        blindLabel.setVisible(false);
        blindLabel.setId("addSource_blindLabel");

        Text scriptHintLabel = new Text("Invalid Script");
        scriptHintLabel.setVisible(false);
        scriptHintLabel.setStyle("-fx-font-size: 10; -fx-fill: red;");
        scriptHintLabel.setId("addSource_downloadScript_hint");

        GridPane grid = new GridPane();
        grid.add(nameLabel, 1, 1);
        grid.add(nameField, 2, 1);
        grid.add(scriptLabel, 1, 2);
        grid.add(scriptField, 2, 2);
        grid.add(blindLabel, 1, 3);
        grid.add(scriptHintLabel, 2, 3);
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
                result = new DownloadEntry(nameField.getText(), scriptField.getText());
            }
            return result;
        });

        UriValidationBinding isScriptValid = new UriValidationBinding(scriptField.textProperty());
        scriptHintLabel.visibleProperty().bind(isScriptValid);
        PropertyBiBinding isFormFilled = new PropertyBiBinding(nameField.textProperty(), scriptField.textProperty());
        okBtn.disableProperty().bind(isFormFilled.or(isScriptValid));

        nameField.requestFocus();
    }
}
