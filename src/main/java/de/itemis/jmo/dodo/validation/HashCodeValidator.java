package de.itemis.jmo.dodo.validation;

import de.itemis.jmo.dodo.io.DataSource;

/**
 * Instances of this class are able to calculate the hashcode of a stream of bytes and verify if it
 * is equal to an expected value.
 */
public interface HashCodeValidator {

    /**
     * Verify that the data provided by {@code dataSource} has the expected hash code.
     *
     * @param dataSource
     * @return {@code true} if validation was successful, {@code false} otherwise.
     */
    boolean verify(DataSource dataSource);
}