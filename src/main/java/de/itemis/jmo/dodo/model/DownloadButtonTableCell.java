package de.itemis.jmo.dodo.model;

import static de.itemis.jmo.dodo.util.NodeIdSanitizer.sanitize;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class DownloadButtonTableCell extends TableCell<DownloadEntry, Button> {

    private final Button downloadButton;

    /**
     * Create a new {@link Callback} that can be used together with a {@link TableColumn}.
     *
     * @param buttonLabel - Label of the push button.
     * @param action - Action to run on button activation.
     * @return A new {@link Callback} instance.
     */
    public static Callback<TableColumn<DownloadEntry, Button>, TableCell<DownloadEntry, Button>> forTableColumn(String buttonLabel,
            Consumer<DownloadEntry> action) {
        return param -> new DownloadButtonTableCell(buttonLabel, action);
    }

    private DownloadButtonTableCell(String buttonLabel, Consumer<DownloadEntry> action) {
        downloadButton = new Button(buttonLabel);
        downloadButton.setOnAction(actionEvent -> {
            action.accept(getCurrentModel());
        });
        downloadButton.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (downloadButton.getId() == null) {
                DownloadEntry currentModel = getCurrentModel();
                String newBtnId = "downloadButton_" + sanitize(currentModel.getArtifactName());
                downloadButton.setId(newBtnId);
            }
            setGraphic(downloadButton);
        }
    }

    private DownloadEntry getCurrentModel() {
        return getTableView().getItems().get(getIndex());
    }
}
