package de.itemis.jmo.dodo.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.Property;

/**
 * <p>
 * A {@link BooleanBinding} that is {@code true} if one of two {@link Property Properties} is empty
 * and {@code false} otherwise.
 * </p>
 * <p>
 * Can be used to implement controls that enable or disabled themselves based on property contents.
 * </p>
 */
public class PropertyBiBinding extends BooleanBinding {

    private final Property<?> firstProp;
    private final Property<?> secondProp;

    public PropertyBiBinding(Property<?> firstProp, Property<?> secondProp) {
        this.firstProp = firstProp;
        this.secondProp = secondProp;
        super.bind(firstProp, secondProp);
    }

    @Override
    protected boolean computeValue() {
        Object firstVal = firstProp.getValue();
        Property<?> secondVal = secondProp;
        return firstVal == null || firstVal.toString().isBlank() || secondVal.getValue() == null || secondVal.getValue().toString().isBlank();
    }
}
