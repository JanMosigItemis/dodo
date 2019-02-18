package de.itemis.jmo.dodo.tests.util;

import java.net.URI;

import de.itemis.jmo.dodo.util.InstantiationNotAllowedException;

/**
 * Contains {@link URI} instances ready to be used in tests.
 */
public final class UriTestData {

    public static final URI VALID_URI = URI.create("validUri");

    private UriTestData() {
        throw new InstantiationNotAllowedException();
    }
}
