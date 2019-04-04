package de.itemis.jmo.dodo.model;

import static de.itemis.jmo.dodo.util.NodeIdSanitizer.sanitize;

import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

public final class DownloadButtonTableCell extends TableCell<DownloadEntry, Node> {

    private final StackPane pane;
    private final Button downloadButton;
    private final ProgressBar progressBar;
    private final Text progressText;

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
        pane = new StackPane();
        downloadButton = new Button(buttonLabel);
        progressBar = new ProgressBar();
        progressText = new Text();

        downloadButton.setOnAction(actionEvent -> {
            updateItem(pane, false);
            DownloadEntry currentModel = getCurrentModel();
            currentModel.addDownloadListener(progressPercent -> {
                Platform.runLater(() -> {
                    progressBar.setProgress(progressPercent / 100);
                    progressText.setText(String.format("%.1f", progressPercent));
                    if (progressPercent >= 100.0) {
                        updateItem(downloadButton, false);
                    }
                });
            });
            action.accept(currentModel);

        });
        downloadButton.setMaxWidth(Double.MAX_VALUE);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        pane.getChildren().addAll(progressBar, progressText);
    }

    @Override
    public void updateItem(Node item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (item == null) {
                item = downloadButton;
            }

            String sanitizedArtifactName = sanitize(getCurrentModel().getArtifactName());
            if (item instanceof StackPane) {
                if (progressBar.getId() == null) {
                    progressBar.setId("downloadProgressBar_" + sanitizedArtifactName);
                }
                if (progressText.getId() == null) {
                    progressText.setId("downloadProgressText_" + sanitizedArtifactName);
                }
            } else if (item instanceof Button) {
                item.setId("downloadButton_" + sanitizedArtifactName);
            }
            setGraphic(item);
        }
    }

    private DownloadEntry getCurrentModel() {
        return getTableView().getItems().get(getIndex());
    }
}
