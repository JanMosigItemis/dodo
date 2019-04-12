package de.itemis.jmo.dodo.tests.util;

import static de.itemis.jmo.dodo.tests.util.JsonTestHelper.toDoubleQuotes;

import java.net.URI;

public class FakeServerDownload {

    private final URI artifactUri;
    private final URI artifactHashCodeUri;
    private final String artifactHashCodeName;

    public FakeServerDownload(URI artifactUri, String artifactHashCodeName, URI artifactHashCodeUri) {
        this.artifactUri = artifactUri;
        this.artifactHashCodeUri = artifactHashCodeUri;
        this.artifactHashCodeName = artifactHashCodeName;
    }

    public URI getArtifactUri() {
        return artifactUri;
    }

    public URI getArtifactHashCodeUri() {
        return artifactHashCodeUri;
    }

    public String getArtifactHashCodeName() {
        return artifactHashCodeName;
    }

    public String getDownloadJson() {
        return toDoubleQuotes("{'uri':'" + artifactUri + "', 'hashAlgorithm':'MD5', 'hashUri':'" + artifactHashCodeUri + "'}");
    }
}
