package de.itemis.jmo.dodo.validation;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.io.DataSource;

/**
 * Instances of this class are able to calculate the checksum of a stream of bytes and verify if it
 * is equal to an expected value.
 */
public interface ChecksumValidator {

    /**
     * Verify that the data provided by {@code dataSource} matches the expected checksum.
     *
     * @param dataSource
     * @return {@code true} if validation was successful, {@code false} otherwise.
     * @throws DodoException in case reading from {@code dataSource} causes an error.
     */
    boolean verify(DataSource dataSource);
}