package de.itemis.jmo.dodo;

import java.net.URI;
import java.util.Optional;

import de.itemis.jmo.dodo.model.DownloadButtonTableCell;
import de.itemis.jmo.dodo.model.DownloadEntry;
import de.itemis.jmo.dodo.model.FakeCellValueFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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

        itemTable.getColumns().add(artifactNameCol);
        itemTable.getColumns().add(isDownloadedCol);
        itemTable.getColumns().add(downloadBtnCol);
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
        addSourceMenuItem.setOnAction(event -> displayAddSourceDialog().map(newEntry -> items.add(newEntry)));
        dodoMenu.getItems().add(addSourceMenuItem);
        mainMenu.getMenus().add(dodoMenu);

        VBox root = new VBox(mainMenu, itemTable);
        Scene mainScene = new Scene(root, 300, 250);
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    private Optional<DownloadEntry> displayAddSourceDialog() {
        Dialog<DownloadEntry> dialog = new Dialog<>();
        dialog.setTitle("Add Source");
        dialog.setHeaderText("Please enter the new source's data.");
        dialog.setResizable(true);

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
        dialog.getDialogPane().setContent(grid);

        ButtonType cancelBtnType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(cancelBtnType);
        Node cancelBtn = dialog.getDialogPane().lookupButton(cancelBtnType);
        cancelBtn.setId("addSource_cancel");

        ButtonType okBtnType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okBtnType);
        Node okBtn = dialog.getDialogPane().lookupButton(okBtnType);
        okBtn.setId("addSource_confirm");

        dialog.setResultConverter(clickedBtn -> {
            DownloadEntry result = null;
            if (clickedBtn == okBtnType) {
                result = new DownloadEntry(nameField.getText(), URI.create(uriField.getText()));
            }
            return result;
        });

        nameField.requestFocus();

        return dialog.showAndWait();
    }
}
