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
    public void binding_isTrue_if_firstTextField_is_Empty() {
        firstProp.setValue("");
        secondProp.setValue("second");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isTrue_if_secondTextField_is_Empty() {
        firstProp.setValue("first");
        secondProp.setValue("");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isTrue_if_bothTextFields_are_Empty() {
        firstProp.setValue("");
        secondProp.setValue("");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isTrue_if_firstTextField_is_null() {
        firstProp.setValue("");
        secondProp.setValue("second");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isTrue_if_secondTextField_is_null() {
        firstProp.setValue("first");
        secondProp.setValue("");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isTrue_if_bothTextFields_are_null() {
        firstProp.setValue("");
        secondProp.setValue("");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isTrue_if_firstTextField_is_blank() {
        firstProp.setValue("   ");
        secondProp.setValue("second");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isTrue_if_secondTextField_is_blank() {
        firstProp.setValue("first");
        secondProp.setValue("   ");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isTrue_if_bothTextFields_are_blank() {
        firstProp.setValue("    ");
        secondProp.setValue("   ");
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void binding_isFalse_if_bothTextFields_are_Filled() {
        firstProp.setValue("first");
        secondProp.setValue("second");
        assertThat(underTest.get()).isFalse();
    }

    @Test
    public void binding_changes_ifFirstField_becomes_invalid() {
        firstProp.setValue("first");
        secondProp.setValue("second");
        boolean oldValue = underTest.get();
        firstProp.setValue(null);
        boolean newValue = underTest.get();
        assertThat(oldValue).isNotEqualTo(newValue);
    }

    @Test
    public void binding_changes_ifSecondField_becomes_invalid() {
        firstProp.setValue("first");
        secondProp.setValue("second");
        boolean oldValue = underTest.get();
        secondProp.setValue(null);
        boolean newValue = underTest.get();
        assertThat(oldValue).isNotEqualTo(newValue);
    }

}
