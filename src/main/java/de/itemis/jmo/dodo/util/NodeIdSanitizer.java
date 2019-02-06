package de.itemis.jmo.dodo.util;

/**
 * JavaFX node ids are somewhat special, because, they do also serve as CSS selectors (like the HTML
 * id attribute). Because of this, some special CSS related characters (e. g. dots) are not allowed.
 * This sanitizer helps us with getting things right.
 */
public final class NodeIdSanitizer {

    /**
     * Sanitize the provided {@code id}.
     *
     * @param id - The raw id to sanitize.
     * @return The sanitized id.
     */
    public static String sanitize(String id) {
        return id.replace('.', '_');
    }

}
