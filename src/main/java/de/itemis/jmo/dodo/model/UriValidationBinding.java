package de.itemis.jmo.dodo.model;

import java.net.URI;

import de.itemis.jmo.dodo.parsing.StringParser;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

/**
 * <p>
 * A binding that is {@code false} if a {@link String} cannot be transformed into a {@link URI} and
 * {@code true} otherwise.
 * </p>
 * <p>
 * Thought to be used together with {@link TextField#disabledProperty()}, hence the inverted logic.
 * </p>
 */
public class UriValidationBinding extends BooleanBinding {

    private final StringProperty prop;
    private final StringParser<?> parser;

    public UriValidationBinding(StringProperty prop, StringParser<?> parser) {
        this.prop = prop;
        this.parser = parser;
        super.bind(prop);
    }

    @Override
    protected boolean computeValue() {
        String rawValue = prop.getValue();
        boolean isInvalid = false;
        try {
            parser.parse(rawValue);
        } catch (Exception e) {
            isInvalid = true;
        }

        return isInvalid;
    }
}
