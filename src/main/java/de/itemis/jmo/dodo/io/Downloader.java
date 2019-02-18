package de.itemis.jmo.dodo.io;

import java.io.InputStream;
import java.net.URI;

public interface Downloader {

    /**
     * Open a new {@link InputStream} on the provied {@link URI}.
     *
     * @param artifactUri - A valid URI that points to a downloadable artifact.
     * @return - A new instance of {@link InputStream}.
     */
    InputStream openStream(URI artifactUri);

}
