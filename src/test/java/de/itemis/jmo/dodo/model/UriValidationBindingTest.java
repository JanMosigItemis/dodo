package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.itemis.jmo.dodo.tests.util.FakeStringToDownloadScriptParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UriValidationBindingTest {

    private static final String VALID_VALUE = "validValue";
    private static final String INVALID_VALUE = "invalidValue";

    private StringProperty prop;
    private FakeStringToDownloadScriptParser parser = new FakeStringToDownloadScriptParser(INVALID_VALUE);

    private UriValidationBinding underTest;

    @BeforeEach
    public void setUp() {
        prop = new SimpleStringProperty();
        underTest = new UriValidationBinding(prop, parser);
    }

    @Test
    public void false_if_valueIsValid() {
        prop.setValue(VALID_VALUE);
        assertThat(underTest.get()).isFalse();
    }

    @Test
    public void true_if_valueIsInvalid() {
        prop.setValue(INVALID_VALUE);
        assertThat(underTest.get()).isTrue();
    }

    @Test
    public void changeFrom_invalid_to_valid_isRecognized() {
        prop.setValue(INVALID_VALUE);
        boolean oldValue = underTest.get();
        prop.setValue(VALID_VALUE);
        boolean newValue = underTest.get();

        assertThat(oldValue).isNotEqualTo(newValue);
    }
}
