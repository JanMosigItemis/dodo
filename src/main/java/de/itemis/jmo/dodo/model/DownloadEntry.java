package de.itemis.jmo.dodo.model;


import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.error.DodoWarning;
import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.io.DownloadResult;
import de.itemis.jmo.dodo.io.Persistence;
import de.itemis.jmo.dodo.io.ProgressListener;

/**
 * An entry of the table of downloadable content.
 */
public class DownloadEntry {

    private final String artifactName;
    private final DownloadScript downloadScript;
    private final Persistence persistence;
    private final List<ProgressListener<Double>> downloadListeners = new ArrayList<>();

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
     * @throws DodoException - In case of any kind of (IO) error during download or writing.
     */
    public DownloadResult download(Path targetPath) {
        var download = downloadScript.createDownload();

        writeDownloadToPersistence(targetPath, download);

        var validator = downloadScript.createHashCodeValidator();
        DataSource dataSource = persistence.read(targetPath);
        boolean hashCodeValidationResult = validator.verify(dataSource);
        try {
            dataSource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        downloadFinished = true;

        return new DownloadResult(hashCodeValidationResult);
    }

    private void writeDownloadToPersistence(Path targetPath, DodoDownload download) {
        DataSource dataSource = null;
        try {
            dataSource = download.getDataSource();
            persistence.write(dataSource, targetPath, writtenBytes -> {
                long totalBytes = download.getSize();
                double percentage = ((double) writtenBytes / totalBytes) * 100;
                updateDownloadProgress(percentage);
            });
        } finally {
            try {
                if (dataSource != null) {
                    dataSource.close();
                }
            } catch (Exception e) {
                throw new DodoWarning("Could not close data source.", e);
            }
        }
    }

    private void updateDownloadProgress(double percentage) {
        downloadListeners.forEach(listener -> listener.updateProgress(percentage));
    }

    public void addDownloadListener(ProgressListener<Double> listener) {
        downloadListeners.add(listener);
    }

    /**
     * @return {@code true} if a download has been finished. {@code false} otherwise.
     */
    public boolean isDownloadFinished() {
        return downloadFinished;
    }
}
