package de.itemis.jmo.dodo.model;

import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;

/**
 * <p>
 * Describes how to download and verify an artifact.
 * </p>
 * <p>
 * The result of parsing a download script.
 * </p>
 */
public class DownloadScript {

    private final Supplier<DodoDownload> downloadSup;

    /**
     * Create a new instance.
     *
     * @param downloadSup - Creates new download instances.
     */
    public DownloadScript(Supplier<DodoDownload> downloadSup) {
        this.downloadSup = downloadSup;
    }

    /**
     * "Execute" the script and create a {@link DodoDownload} that can be used to actually run the
     * download of data.
     *
     * @return - A new instance of {@link DodoDownload}.
     */
    public DodoDownload createDownload() {
        return downloadSup.get();
    }
}
