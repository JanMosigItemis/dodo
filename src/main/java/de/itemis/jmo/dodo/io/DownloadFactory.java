package de.itemis.jmo.dodo.io;

import java.net.URI;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * A factory that can create "downloadable" objects from {@link URI}.
 */
public interface DownloadFactory {

    /**
     * Create a {@link DodoDownload} from a {@link URI}. If the URI's scheme is not supported or
     * absent, a {@link NopDownload} instance will be returned.
     *
     * @param uri - Data will be retrieved from the resource this URI is pointing to.
     * @return - A new instance of {@link DodoDownload}.
     * @throws DodoException In case the provided {@code uri} is insufficient depending on the
     *         judgement of the implementation.
     */
    DodoDownload createDownload(URI uri);
}
