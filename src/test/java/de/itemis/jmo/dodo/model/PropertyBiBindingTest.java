package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PropertyBiBindingTest {

    private StringProperty firstProp;
    private StringProperty secondProp;

    private PropertyBiBinding underTest;

    @BeforeEach
    public void setUp() {
        firstProp = new SimpleStringProperty();
        secondProp = new SimpleStringProperty();
        underTest = new PropertyBiBinding(firstProp, secondProp);
    }

    @Test
    public void binding_isFalse_if_firstTextField_is_Empty() {
        firstProp.setValue(null);
        secondProp.setValue("second");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isFalse_if_secondTextField_is_Empty() {
        firstProp.setValue("first");
        secondProp.setValue(null);
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_becomes_invalid_ifFirstField_gets_empty() {
        firstProp.setValue("first");
        secondProp.setValue("second");
        assertThat(underTest.get()).isFalse();
        firstProp.setValue(" ");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_becomes_invalid_ifSecondField_gets_empty() {
        firstProp.setValue("first");
        secondProp.setValue("second");
        assertThat(underTest.get()).isFalse();
        secondProp.setValue(" ");
        assertThat(underTest.get()).isTrue();
    }
}
