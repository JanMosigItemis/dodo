package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn.CellDataFeatures;

public class FakeCellValueFactoryTest {

    private FakeCellValueFactory<DownloadEntry, Button> underTest;

    @BeforeEach
    public void setUp() {
        underTest = new FakeCellValueFactory<>();
    }

    @Test
    public void produces_default_value_for_an_object() {
        var cellDataFeatures = new CellDataFeatures<DownloadEntry, Button>(null, null, null);
        ObservableValue<Button> result = underTest.call(cellDataFeatures);
        assertThat(result.getValue()).isNull();
    }
}
