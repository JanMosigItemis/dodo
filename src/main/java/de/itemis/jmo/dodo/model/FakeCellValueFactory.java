package de.itemis.jmo.dodo.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * <p>
 * Some {@link TableView} columns contain "fake" data, e. g. push buttons. In order to comply with
 * the JavaFX model (and to be able to test things in a reasonable way), these columns need to have
 * a fake value set as well. This value will always be the same for these kind of cells and will be
 * created by an instance of this class.
 * </p>
 * <p>
 * It is thought to be used in conjunction with
 * {@link TableColumn#setCellValueFactory(javafx.util.Callback)}
 * </p>
 */
public final class FakeCellValueFactory<S, R> implements Callback<TableColumn.CellDataFeatures<S, R>, ObservableValue<R>> {

    @Override
    public ObservableValue<R> call(CellDataFeatures<S, R> param) {
        return new SimpleObjectProperty<>();
    }
}
