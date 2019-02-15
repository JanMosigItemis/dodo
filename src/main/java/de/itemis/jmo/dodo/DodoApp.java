package de.itemis.jmo.dodo;

import java.net.URI;

import de.itemis.jmo.dodo.model.DeleteButtonTableCell;
import de.itemis.jmo.dodo.model.DownloadButtonTableCell;
import de.itemis.jmo.dodo.model.DownloadEntry;
import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.model.FakeCellValueFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Hello world!
 */
public class DodoApp extends Application {

    private final TableView<DownloadEntry> itemTable = new TableView<DownloadEntry>();
    private final ObservableList<DownloadEntry> items = FXCollections.observableArrayList();

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

        TableColumn<DownloadEntry, Button> deleteBtnCol = new TableColumn<>("Delete");
        deleteBtnCol.setCellValueFactory(new FakeCellValueFactory<>());
        deleteBtnCol.setCellFactory(DeleteButtonTableCell.forTableColumn("Delete", entry -> {
            itemTable.getItems().remove(entry);
        }));

        itemTable.getColumns().add(artifactNameCol);
        itemTable.getColumns().add(isDownloadedCol);
        itemTable.getColumns().add(downloadBtnCol);
        itemTable.getColumns().add(deleteBtnCol);
        itemTable.setEditable(false);
        itemTable.setId("itemTable");
        itemTable.setItems(items);

        Stage mainStage = new Stage();
        mainStage.setTitle("Dodo");
        mainStage.initStyle(StageStyle.DECORATED);

        MenuBar mainMenu = new MenuBar();
        Menu dodoMenu = new Menu("Dodo");
        dodoMenu.setId("dodoMenu");
        MenuItem addSourceMenuItem = new MenuItem("Add Source..");
        addSourceMenuItem.setId("addSource");
        addSourceMenuItem.setOnAction(
            event -> new AddDownloadSourceDialog(script -> new DownloadScript(URI.create("fakeUri"))).showAndWait().map(newEntry -> items.add(newEntry)));
        dodoMenu.getItems().add(addSourceMenuItem);
        mainMenu.getMenus().add(dodoMenu);

        VBox root = new VBox(mainMenu, itemTable);
        Scene mainScene = new Scene(root, 400, 250);
        mainStage.setScene(mainScene);
        mainStage.show();
    }
}
