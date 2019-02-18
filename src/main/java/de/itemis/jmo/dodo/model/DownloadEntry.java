package de.itemis.jmo.dodo.model;


import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import de.itemis.jmo.dodo.error.DodoWarning;
import de.itemis.jmo.dodo.io.Persistence;

/**
 * An entry of the table of downloadable content.
 */
public class DownloadEntry {

    private final String artifactName;
    private final DownloadScript downloadScript;
    private final Persistence persistence;

    private boolean downloadFinished = false;

    /**
     * Create a new instance.
     *
     * @param artifactName - Name of the entry.
     * @param downloadScript - How to access the artifact.
     * @param persistence - Used for storing / retrieving downloaded data.
     */
    public DownloadEntry(String artifactName, DownloadScript downloadScript, Persistence persistence) {
        this.artifactName = requireNonNull(artifactName, "artifactName");
        this.downloadScript = requireNonNull(downloadScript, "downloadScript");
        this.persistence = persistence;
    }

    /**
     * @return the artifact's name.
     */
    public String getArtifactName() {
        return artifactName;
    }

    /**
     * Perform the download. Blocks until the download has been finished.
     *
     * @param path - Store all downloaded data into this file.
     */
    public void download(Path targetPath) {
        try (InputStream artifactStream = downloadScript.createDownload()) {
            persistence.write(targetPath, artifactStream);
        } catch (IOException e) {
            throw new DodoWarning("Error while closing stream.", e);
        }
        downloadFinished = true;
    }

    /**
     * @return {@code true} if a download has been finished. {@code false} otherwise.
     */
    public boolean isDownloadFinished() {
        return downloadFinished;
    }
}
