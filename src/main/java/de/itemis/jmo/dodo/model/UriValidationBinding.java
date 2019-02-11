package de.itemis.jmo.dodo.model;

import java.net.URI;

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

    public UriValidationBinding(StringProperty prop) {
        this.prop = prop;
        super.bind(prop);
    }

    @Override
    protected boolean computeValue() {
        String rawValue = prop.getValue();
        boolean isInvalid = false;
        try {
            URI.create(rawValue);
        } catch (IllegalArgumentException | NullPointerException e) {
            isInvalid = true;
        }

        return isInvalid;
    }
}
