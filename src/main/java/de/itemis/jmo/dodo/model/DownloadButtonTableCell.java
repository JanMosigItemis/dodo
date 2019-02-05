package de.itemis.jmo.dodo.model;

import java.util.function.Function;

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
            Function<DownloadEntry, DownloadEntry> action) {
        return param -> new DownloadButtonTableCell(buttonLabel, action);
    }

    private DownloadButtonTableCell(String buttonLabel, Function<DownloadEntry, DownloadEntry> action) {
        downloadButton = new Button(buttonLabel);
        downloadButton.setId("downloadButton");
        downloadButton.setOnAction(actionEvent -> {
            action.apply(getCurrentItem());
        });
        downloadButton.setMaxWidth(Double.MAX_VALUE);
    }

    private DownloadEntry getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(downloadButton);
        }
    }
}
