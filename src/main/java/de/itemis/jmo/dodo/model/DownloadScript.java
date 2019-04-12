package de.itemis.jmo.dodo.model;

import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.validation.HashCodeValidator;

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
    private final Supplier<HashCodeValidator> hashCodeValidatorSup;

    /**
     * Create a new instance.
     *
     * @param downloadSup - Creates new download instances.
     * @param hashCodeValidatorSup - Creates new {@link HashCodeValidator} instances.
     */
    public DownloadScript(Supplier<DodoDownload> downloadSup, Supplier<HashCodeValidator> hashCodeValidatorSup) {
        this.downloadSup = downloadSup;
        this.hashCodeValidatorSup = hashCodeValidatorSup;
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

    /**
     * Provides a validator for the downloaded data.
     *
     * @return a new HashCodeValidator instance.
     */
    public HashCodeValidator createHashCodeValidator() {
        return hashCodeValidatorSup.get();
    }
}
