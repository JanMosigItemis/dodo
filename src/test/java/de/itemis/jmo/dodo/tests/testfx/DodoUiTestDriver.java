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
     * @param downloadScript - Download script of the artifact.
     */
    void addDownloadSource(String artifactName, String downloadScript);

    /**
     * Initiate a download via UI.
     */
    void download(String artifactName);

    /**
     * Remove an entry via UI.
     */
    void delete(String artifactName);

    /**
     * Assert that the UI indicates a successful download of the artifact of the specified name.
     */
    void assertDownloadSuccessIndicated(String artifactName);

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
}