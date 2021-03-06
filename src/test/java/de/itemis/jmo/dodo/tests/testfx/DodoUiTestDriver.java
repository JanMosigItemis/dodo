package de.itemis.jmo.dodo.tests.testfx;

/**
 * A "driver" for UI tests of the whole Dodo app. It decouples the actual test code ("what to test")
 * from the knowledge on how to actually perform those tests. This helps keeping the tests compiling
 * in case test technology or app technology changes.
 */
public interface DodoUiTestDriver extends DodoTestsJunit5Lifecycle {

    /**
     * When this method returns, Dodo knows about another download source.
     *
     * @param artifactName - Name of the artifact provided by source.
     */
    void addDownloadSource(String artifactName);

    /**
     * Initiate a download via UI. This method does only block in case the UI blocks during
     * download.
     *
     * @param artifactName - Download the artifact known under this name.
     */
    void initiateDownload(String artifactName);

    /**
     * Remove an entry via UI.
     */
    void delete(String artifactName);

    /**
     * Assert that the UI indicates a successful download of the artifact of the specified name.
     */
    void assertDownloadSuccessIndicated(String artifactName);

    /**
     * Block until the specified artifact has been downloaded.
     *
     * @throws AssertionError - In case waiting times out.
     */
    void waitUntilDownloadFinished(String artifactName);

    /**
     * Assert that the UI does not display any download entries.
     */
    void assertNoDownloadEntriesDisplayed();

    /**
     * Assert that the download entry matching the provided {@code artifactName} is being displayed.
     *
     * @param artifactName - The name of the artifact behind the entry.
     */
    void assertDownloadEntryDisplayed(String artifactName);

    /**
     * Assert that the downloaded artifact has been successfully stored into the correct place.
     *
     * @param artifactName - Identifier of the downloaded artifact.
     */
    void assertDownloadStored(String artifactName);

    /**
     * The next call to {@link #initiateDownload(String)} will cause the download to become stall
     * when {@code percentage} of the specified artifact have been downloaded.
     *
     * @param artifactName
     *
     * @param percentage - Stall as soon as this percentage has been downloaded.
     * @param artifactName - Name of artifact for which to stall the download.
     */
    void letDownloadStallAt(String artifactName, int percentage);

    /**
     * Assert that the download progress bar for the specified artifact is at {@code percentage}.
     *
     * @param percentage - Expected progress in percent.
     * @param artifactName - Name of artifact for which to perform the assert.
     */
    void assertDownloadProgressBarAt(String artifcatName, int percentage);

    /**
     * Assert that the download progress for the specified artifact is readable as text and has the
     * specified expected value.
     *
     * @param expectedText - Expected progress value.
     * @param artifactName - Name of artifact for which to perform the assert.
     */
    void assertDownloadProgressText(String artifactName, String expectedText);
}