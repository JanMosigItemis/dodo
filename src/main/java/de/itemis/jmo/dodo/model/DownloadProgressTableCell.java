package de.itemis.jmo.dodo.model;

import static de.itemis.jmo.dodo.util.NodeIdSanitizer.sanitize;

import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public final class DownloadProgressTableCell extends TableCell<DownloadEntry, Node> {

    private final ProgressBar progressBar;

    /**
     * Create a new {@link Callback} that can be used in conjunction with
     * {@link TableColumn#setCellFactory(Callback)}.
     *
     * @return A new {@link Callback} instance.
     */
    public static Callback<TableColumn<DownloadEntry, Node>, TableCell<DownloadEntry, Node>> forTableColumn() {
        return param -> new DownloadProgressTableCell();
    }

    private DownloadProgressTableCell() {
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    public void updateItem(Node item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (progressBar.getId() == null) {
                DownloadEntry currentModel = getCurrentModel();
                String newPbId = "downloadProgress_" + sanitize(currentModel.getArtifactName());
                progressBar.setId(newPbId);
            }
            setGraphic(progressBar);
        }
    }

    private DownloadEntry getCurrentModel() {
        return getTableView().getItems().get(getIndex());
    }
}
