package de.itemis.jmo.dodo.model;


import static java.util.Objects.requireNonNull;

/**
 * An entry of the table of downloadable content.
 */
public class DownloadEntry {

    private final String artifactName;
    private final String downloadScript;

    private boolean downloadFinished = false;

    /**
     * Create a new instance.
     *
     * @param artifactName - Name of the entry.
     * @param downloadScript - How to access the artifact.
     */
    public DownloadEntry(String artifactName, String downloadScript) {
        this.artifactName = requireNonNull(artifactName, "artifactName");
        this.downloadScript = requireNonNull(downloadScript, "downloadScript");
    }

    /**
     * @return the artifact's name.
     */
    public String getArtifactName() {
        return artifactName;
    }

    /**
     * @return the script with which to access the artifact.
     */
    public String getDownloadScript() {
        return downloadScript;
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
