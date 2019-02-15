package de.itemis.jmo.dodo.parsing;

/**
 * Instances of this interface are capable of parsing a {@link String} into a {@code T}.
 *
 * @param <T> - Result type of the parse operation.
 */
@FunctionalInterface
public interface StringParser<T> {

    /**
     * Parse the provided {@code text} into a valid instance of {@code T}.
     *
     * @param text - Parse this text.
     * @return A new instance of {@code T} that is an object representation of the provided
     *         {@code text}.
     */
    T parse(String text);

}