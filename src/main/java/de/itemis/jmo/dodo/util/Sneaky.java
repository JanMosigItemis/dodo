package de.itemis.jmo.dodo.util;

/**
 * <p>
 * A helper class to help "sneak" checked exceptions through interfaces that do not (want to)
 * declare any checked exception to be thrown.
 * </p>
 * <p>
 * Was introduced to reduce boiler plate code when implementing code that does not want to handle
 * checked {@link Exception Exceptions} but just wants to get rid of them without loosing
 * information.
 * </p>
 * <p>
 * Uses the "Sneaky Throws" paradigm.
 * </p>
 *
 * @param <RETURN_TYPE> - Type of wrapped code's return type if any.
 * @see <a href=
 *      "http://www.philandstuff.com/2012/04/28/sneakily-throwing-checked-exceptions.html">http://www.philandstuff.com/2012/04/28/sneakily-throwing-checked-exceptions.html</a>
 */
public final class Sneaky<RETURN_TYPE> {


    /**
     * <p>
     * Throw the provided throwable using sneaky throws paradigm. Can be used to throw checked
     * exceptions in methods that do not declare them to be thrown.
     * </p>
     * <p>
     * Example:
     *
     * <pre>
     * public void doSomething() {
     *   - Method will throw IOException even though it has not been declared via throws -
     *   Sneaky.throwThat(new IOException());
     * }
     * </pre>
     * </p>
     *
     * @param t - The {@link Throwable} to (re)throw.
     * @throws T - The type of the provided throwable.
     * @see <a href=
     *      "http://www.philandstuff.com/2012/04/28/sneakily-throwing-checked-exceptions.html">http://www.philandstuff.com/2012/04/28/sneakily-throwing-checked-exceptions.html</a>
     */
    /*
     * In order to use the sneaky throws paradigm here we have to use an "unchecked" cast. However,
     * the case is "safe", because T is inferred to be a RuntimeException (Java type inference
     * rules). Now the compiler sees: "throw (RuntimeException) expectedThrowable" while the runtime
     * sees: "throw expectedThrowable (type erasure)", so the cast disappears at runtime. No cast ->
     * always "safe".
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void throwThat(Throwable t) throws T {
        throw (T) t;
    }
}
