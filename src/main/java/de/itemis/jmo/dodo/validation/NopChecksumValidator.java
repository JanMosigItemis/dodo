package de.itemis.jmo.dodo.validation;

import de.itemis.jmo.dodo.io.DataSource;

/**
 * This {@link ChecksumValidator} implementation always returns {@code false}. Thought to be used as
 * a last resort when creating validators but the algorithm name is not known.
 */
public class NopChecksumValidator implements ChecksumValidator {

    @Override
    public boolean verify(DataSource dataSource) {
        return true;
    }
}
