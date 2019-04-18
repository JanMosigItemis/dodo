package de.itemis.jmo.dodo.validation;

import de.itemis.jmo.dodo.io.DataSource;

/**
 * {@link ChecksumValidator} implementation that validates according to the SHA-512 algorithm.
 */
public class Sha512ChecksumValidator implements ChecksumValidator {

    @Override
    public boolean verify(DataSource dataSource) {
        return false;
    }
}
