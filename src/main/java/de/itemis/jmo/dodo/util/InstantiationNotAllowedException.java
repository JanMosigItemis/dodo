package de.itemis.jmo.dodo.util;

/**
 * Use this in private constructors of static helper classes, e. g.:
 *
 * <pre>
 * public final class SomeHelper {
 *     private SomeHelper() {
 *         throw new InstantiationNotAllowedException();
 *     }
 *
 *     // static helper methods below
 * }
 * </pre>
 */
public class InstantiationNotAllowedException extends UnsupportedOperationException {

    private static final long serialVersionUID = 1L;

    public InstantiationNotAllowedException() {
        super("Instantiation not allowed.");
    }
}
