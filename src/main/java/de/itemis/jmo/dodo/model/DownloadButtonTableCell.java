package de.itemis.jmo.dodo.model;

import static de.itemis.jmo.dodo.util.NodeIdSanitizer.sanitize;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class DownloadButtonTableCell extends TableCell<DownloadEntry, Node> {

    private final Button downloadButton;
    private final ProgressBar progressBar;

    /**
     * Create a new {@link Callback} that can be used in conjunction with
     * {@link TableColumn#setCellFactory(Callback)}.
     *
     * @param buttonLabel - Label of the push button.
     * @param action - Action to run on button activation.
     * @return A new {@link Callback} instance.
     */
    public static Callback<TableColumn<DownloadEntry, Node>, TableCell<DownloadEntry, Node>> forTableColumn(String buttonLabel,
            Consumer<DownloadEntry> action) {
        return param -> new DownloadButtonTableCell(buttonLabel, action);
    }

    private DownloadButtonTableCell(String buttonLabel, Consumer<DownloadEntry> action) {
        downloadButton = new Button(buttonLabel);
        progressBar = new ProgressBar();
        downloadButton.setOnAction(actionEvent -> {
            updateItem(progressBar, false);
            DownloadEntry currentModel = getCurrentModel();
            currentModel.addDownloadListener(progress -> {
                Platform.runLater(() -> {
                    progressBar.setProgress(progress / 100);
                    if (progress == 100.0) {
                        updateItem(downloadButton, false);
                    }
                });
            });
            action.accept(currentModel);

        });
        downloadButton.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    public void updateItem(Node item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            String nodeIdPrefix = "downloadButton_";
            if (item == null) {
                item = downloadButton;
            }

            if (item instanceof ProgressBar) {
                nodeIdPrefix = "downloadProgress_";
            }

            if (item.getId() == null) {
                String newBtnId = nodeIdPrefix + sanitize(getCurrentModel().getArtifactName());
                item.setId(newBtnId);
            }
            System.err.println(item);
            setGraphic(item);
        }
    }

    private DownloadEntry getCurrentModel() {
        return getTableView().getItems().get(getIndex());
    }
}
