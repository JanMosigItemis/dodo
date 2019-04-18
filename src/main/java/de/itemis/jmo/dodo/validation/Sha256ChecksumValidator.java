package de.itemis.jmo.dodo.validation;

import de.itemis.jmo.dodo.io.DataSource;

/**
 * {@link ChecksumValidator} implementation that validates according to the SHA-256 algorithm.
 */
public class Sha256ChecksumValidator implements ChecksumValidator {

    @Override
    public boolean verify(DataSource dataSource) {
        return false;
    }
}
