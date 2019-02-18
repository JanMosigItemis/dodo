package de.itemis.jmo.dodo.model;

import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

import de.itemis.jmo.dodo.io.Downloader;

/**
 * <p>
 * Describes how to download and verify an artifact.
 * </p>
 * <p>
 * The result of parsing a download script.
 * </p>
 */
public class DownloadScript {

    private final URI artifactUri;
    private final Downloader downloader;

    /**
     * Create a new instance.
     *
     * @param artifactUri
     */
    public DownloadScript(URI artifactUri, Downloader downloader) {
        this.artifactUri = artifactUri;
        this.downloader = downloader;
    }

    /**
     * "Execute" the script and create an {@link InputStream} that can be used to read downloadable
     * bytes of the artifact.
     *
     * @return - A new instance of {@link InputStream}.
     */
    public InputStream createDownload() {
        return downloader.openStream(artifactUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactUri);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DownloadScript other = (DownloadScript) obj;
        return Objects.equals(artifactUri, other.artifactUri);
    }
}
