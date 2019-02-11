package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UriValidationBindingTest {

    private StringProperty prop;
    private UriValidationBinding underTest;

    @BeforeEach
    public void setUp() {
        prop = new SimpleStringProperty();
        underTest = new UriValidationBinding(prop);
    }

    @Test
    public void false_if_uriIsValid() {
        prop.setValue("validUri");
        assertThat(underTest.get()).isFalse();
    }

    @Test
    public void true_if_uriIsBlank() {
        prop.setValue(" ");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void true_if_uriIsNull() {
        prop.setValue(null);
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void true_if_uriIsInvalid() {
        prop.setValue("-:invalid##/uri");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void changeFrom_invalid_to_valid_isRecognized() {
        prop.setValue(null);
        boolean oldValue = underTest.get();
        prop.setValue("uri");
        boolean newValue = underTest.get();

        assertThat(oldValue).isNotEqualTo(newValue);
    }
}
