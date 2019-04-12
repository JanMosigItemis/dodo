package de.itemis.jmo.dodo.io;

/**
 * Download result report. Contains various information on how the download and data verification
 * went.
 */
public class DownloadResult {

    private final boolean hashCodeValidationResult;

    /**
     * Create a new instance.
     *
     * @param hashCodeValidationResult - Is the downloaded data's hash code valid?
     */
    public DownloadResult(boolean hashCodeValidationResult) {
        this.hashCodeValidationResult = hashCodeValidationResult;
    }

    /**
     * @return {@code true} if the hash code verification of the downloaded data was successful,
     *         {@code false} otherwise.
     */
    public boolean hashIsValid() {
        return hashCodeValidationResult;
    }
}
