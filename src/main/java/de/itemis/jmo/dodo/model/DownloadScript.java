package de.itemis.jmo.dodo.model;

import java.net.URI;
import java.util.Objects;

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

    /**
     * Create a new instance.
     *
     * @param artifactUri
     */
    public DownloadScript(URI artifactUri) {
        this.artifactUri = Objects.requireNonNull(artifactUri, "artifactUri");
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
