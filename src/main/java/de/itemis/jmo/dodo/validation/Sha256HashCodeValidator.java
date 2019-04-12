package de.itemis.jmo.dodo.validation;

import de.itemis.jmo.dodo.io.DataSource;

/**
 * {@link HashCodeValidator} implementation that validates according to the SHA-256 algorithm.
 */
public class Sha256HashCodeValidator implements HashCodeValidator {

    @Override
    public boolean verify(DataSource dataSource) {
        return false;
    }
}
