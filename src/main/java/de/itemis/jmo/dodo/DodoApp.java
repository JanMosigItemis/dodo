package de.itemis.jmo.dodo;

import java.util.Objects;

import de.itemis.jmo.dodo.model.DownloadButtonTableCell;
import de.itemis.jmo.dodo.model.DownloadEntry;
import de.itemis.jmo.dodo.model.FakeCellValueFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Hello world!
 */
public class DodoApp extends Application {

    private final TableView<DownloadEntry> itemTable = new TableView<DownloadEntry>();
    private final ObservableList<DownloadEntry> items;

    /**
     * Initialize with no content.
     */
    public DodoApp() {
        this.items = FXCollections.observableArrayList();
    }

    /**
     * Initialize with content.
     *
     * @param items - Items to show.
     */
    public DodoApp(ObservableList<DownloadEntry> items) {
        Objects.requireNonNull(items, "items");
        this.items = items; // FXCollections.observableArrayList(items);
    }

    /**
     * We are not going to use the {@code primaryStage} here, because it may have already got its
     * style set. Styles cannot be changed if a stage has been made visible, so in order to prevent
     * for
     * {@code java.lang.IllegalStateException: Cannot set style once stage has been set visible}, we
     * do create our own stage here.
     */
    @Override
    public void start(Stage primaryStage) {
        TableColumn<DownloadEntry, String> artifactNameCol = new TableColumn<>("Name");
        artifactNameCol.setCellValueFactory(
            new PropertyValueFactory<DownloadEntry, String>("artifactName"));

        TableColumn<DownloadEntry, Boolean> isDownloadedCol = new TableColumn<>("Download Finished");
        isDownloadedCol.setCellValueFactory(
            new PropertyValueFactory<DownloadEntry, Boolean>("downloadFinished"));

        TableColumn<DownloadEntry, Button> downloadBtnCol = new TableColumn<>("Download");
        downloadBtnCol.setCellValueFactory(new FakeCellValueFactory<>());
        downloadBtnCol.setCellFactory(DownloadButtonTableCell.forTableColumn("Download", entry -> {
            entry.download();
            itemTable.refresh();
        }));

        itemTable.getColumns().add(artifactNameCol);
        itemTable.getColumns().add(isDownloadedCol);
        itemTable.getColumns().add(downloadBtnCol);
        itemTable.setEditable(false);
        itemTable.setId("itemTable");
        itemTable.setItems(items);

        Stage mainStage = new Stage();
        mainStage.setTitle("Dodo");
        mainStage.initStyle(StageStyle.DECORATED);

        StackPane root = new StackPane();
        root.getChildren().add(itemTable);
        mainStage.setScene(new Scene(root, 300, 250));
        mainStage.show();
    }
}
