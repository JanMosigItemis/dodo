package de.itemis.jmo.dodo.tests;

import java.net.URI;


public interface DodoTestRunner {

    /**
     * Call this method in your @BeforeEach test initialization method.
     */
    void beforeEach();

    /**
     * Call this method in your @AfterEach test initialization method.
     */
    void afterEach();

    /**
     * When this method returns, Dodo knows about another download source.
     *
     * @param artifactName - Name of the artifact provided by source.
     * @param artifactUri - Download uri of the artifact.
     */
    void addDownloadSource(String artifactName, URI artifactUri);

    /**
     * Initiate a download via UI.
     */
    void download(String artifactName);

    /**
     * Assert that the UI indicates a successful download of the artifact of the specified name.
     */
    void assertDownloadSuccessIndicated(String artifactName);

}