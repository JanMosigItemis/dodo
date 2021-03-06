package de.itemis.jmo.dodo.model;

import static de.itemis.jmo.dodo.util.NodeIdSanitizer.sanitize;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class DeleteButtonTableCell extends TableCell<DownloadEntry, Button> {

    private final Button button;

    /**
     * Create a new {@link Callback} that can be used in conjunction with
     * {@link TableColumn#setCellFactory(Callback)}.
     *
     * @param buttonLabel - Label of the push button.
     * @param action - Action to run on button activation.
     * @return A new {@link Callback} instance.
     */
    public static Callback<TableColumn<DownloadEntry, Button>, TableCell<DownloadEntry, Button>> forTableColumn(String buttonLabel,
            Consumer<DownloadEntry> action) {
        return param -> new DeleteButtonTableCell(buttonLabel, action);
    }

    private DeleteButtonTableCell(String buttonLabel, Consumer<DownloadEntry> action) {
        button = new Button(buttonLabel);
        button.setOnAction(actionEvent -> {
            action.accept(getCurrentModel());
        });
        button.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (button.getId() == null) {
                DownloadEntry currentModel = getCurrentModel();
                String newBtnId = "deleteButton_" + sanitize(currentModel.getArtifactName());
                button.setId(newBtnId);
            }
            setGraphic(button);
        }
    }

    private DownloadEntry getCurrentModel() {
        return getTableView().getItems().get(getIndex());
    }
}
