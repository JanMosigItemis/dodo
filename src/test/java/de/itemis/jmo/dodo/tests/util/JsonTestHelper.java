package de.itemis.jmo.dodo.tests.util;

import de.itemis.jmo.dodo.util.InstantiationNotAllowedException;

/**
 * Convenience methods that deal with JSON stuff to make test implementation easier.
 */
public final class JsonTestHelper {

    private JsonTestHelper() {
        throw new InstantiationNotAllowedException();
    }

    /**
     * <p>
     * Helps to ease JSON string implementation. Converts single quotes to doubleQuotes.
     * </p>
     * <p>
     * By using this, you can omit escaping double quotes in JSON strings, which is very error
     * prone.
     * </p>
     *
     * <pre>
     * parser.parse(JsonTestHelper.toDoubleQuote("{'key':'value'}"));
     * </pre>
     *
     * @param preJson -
     * @return
     */
    public static String toDoubleQuotes(String preJson) {
        return preJson.replace('\'', '"');
    }
}
