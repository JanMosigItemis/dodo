package de.itemis.jmo.dodo.tests.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import de.itemis.jmo.dodo.util.InstantiationNotAllowedException;

/**
 * Contains {@link URI} instances ready to be used in tests.
 */
public final class UriTestData {

    public static final URI VALID_URI_W_SCHEME = URI.create("http://validUri");
    public static final URL VALID_URL;

    static {
        try {
            VALID_URL = VALID_URI_W_SCHEME.toURL();
        } catch (MalformedURLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static final URI VALID_URI_NO_SCHEME = URI.create("validUri");
    public static final URI OTHER_VALID_URI_NO_SCHEME = URI.create("otherValidUri");

    public static final URI URI_BUT_NO_URL =
        URI.create("http://validUriNoUrl:-1234");

    private UriTestData() {
        throw new InstantiationNotAllowedException();
    }
}
