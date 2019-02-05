package de.itemis.jmo.dodo.model;


import static java.util.Objects.requireNonNull;

import java.net.URI;

/**
 * An entry of the table of downloadable content.
 */
public class DownloadEntry {

    private final String artifactName;
    private final URI artifactUri;

    private boolean downloadFinished = false;

    /**
     * Create a new instance.
     *
     * @param artifactName - Name of the entry.
     * @param artifactUri - Where to access the artifact.
     */
    public DownloadEntry(String artifactName, URI artifactUri) {
        this.artifactName = requireNonNull(artifactName, "artifactName");
        this.artifactUri = requireNonNull(artifactUri, "artifactUri");
    }

    /**
     * @return the artifact's name.
     */
    public String getArtifactName() {
        return artifactName;
    }

    /**
     * @return the URI under which to access the artifact.
     */
    public URI getArtifactUri() {
        return artifactUri;
    }

    /**
     * Perform the download. Blocks until the download has been finished.
     */
    public void download() {
        downloadFinished = true;
    }

    /**
     * @return {@code true} if a download has been finished. {@code false} otherwise.
     */
    public boolean isDownloadFinished() {
        return downloadFinished;
    }
}
