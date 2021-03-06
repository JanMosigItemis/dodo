package de.itemis.jmo.dodo.tests.util;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static de.itemis.jmo.dodo.tests.util.TestHelper.fail;
import static de.itemis.jmo.dodo.tests.util.TestHelper.printWarning;
import static java.util.Arrays.fill;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A "server" that provides arbitrary artifacts for download. Thought to be a replacement for a real
 * server, so that we do not need to download real artifacts from real servers of the Internet.
 */
public final class FakeServer {

    private final Map<String, UUID> registeredArtifacts = new HashMap<>();
    private final DownloadBlockSizeStrategyForTests downloadBlockSizeBytes;

    private URI httpServerBaseUri;
    private WireMockServer wireMockServer;
    private DownloadListener downloadListener;

    /**
     * Construct a new instance.
     *
     * @param downloadBlockSizeStrategy - The block size of dodo downloads. Used to reliably
     *        calculate things when download stall at particular percentages is required. The fake
     *        server may change the next block size as
     */
    public FakeServer(DownloadBlockSizeStrategyForTests downloadBlockSizeStrategy) {
        this.downloadBlockSizeBytes = downloadBlockSizeStrategy;
    }

    /**
     * Make the artifact with the specified name available for download.
     *
     * @return A new instance of {@link FakeServerDownload} along with all necessary information to
     *         actually download the artifact.
     */
    public FakeServerDownload provide(String artifactName) {
        String artifactHashCodeName = artifactName + ".hashcode";
        lazySetupHttpServer();
        unregisterDownloadStub(artifactName);
        unregisterDownloadStub(artifactHashCodeName);
        URI artifactUri = registerDownloadStub(artifactName);
        URI artifactHashCodeUri = registerDownloadStub(artifactHashCodeName);

        return new FakeServerDownload(artifactUri, artifactHashCodeName, artifactHashCodeUri);
    }

    /**
     * Stop the server and delete all resources.
     */
    public void stop() {
        try {
            if (wireMockServer != null) {
                wireMockServer.stop();
            }
        } catch (Exception e) {
            printWarning("Stopping fake server ran into a problem", e);
        } finally {
            wireMockServer = null;
            downloadListener = null;
            httpServerBaseUri = null;
            registeredArtifacts.clear();
        }
    }

    private void lazySetupHttpServer() {
        if (wireMockServer == null) {
            downloadListener = new DownloadListener();
            wireMockServer = new WireMockServer(options().dynamicPort().extensions(downloadListener));

            try {
                wireMockServer.start();
            } catch (Exception e) {
                fail("Could not start test HTTP server.", e);
            }
            httpServerBaseUri = URI.create("http://localhost:" + wireMockServer.port());
        }
    }

    /**
     * @return the size of the artifact in bytes.
     */
    public long getSize(String artifactName) {
        return IoHelperForTests.readBytes(getClass(), "__files/" + artifactName).length;
    }

    public void becomeStall(String artifactName, int percentage) {
        lazySetupHttpServer();

        unregisterDownloadStub(artifactName);

        UUID stubId = UUID.randomUUID();
        long artifactSize = 100;
        downloadBlockSizeBytes.setNext(1);
        byte[] fakeDownload = new byte[(int) Math.round(artifactSize * ((double) percentage / 100))];
        fill(fakeDownload, (byte) 0);

        wireMockServer.stubFor(get("/" + artifactName)
            .withId(stubId)
            .willReturn(aResponse()
                .withBody(fakeDownload)
                .withHeader("Content-Length", "" + artifactSize))
            .withPostServeAction(downloadListener.getName(), new ArtifactNameParameter(artifactName)));
        registeredArtifacts.put(artifactName, stubId);
    }

    /**
     * Check if a request for the specified artifact has been successfully served.
     */
    public boolean downloadFinished(String artifactName) {
        return downloadListener.requestServed(artifactName);
    }

    private void unregisterDownloadStub(String artifactName) {
        if (registeredArtifacts.containsKey(artifactName)) {
            UUID stubId = registeredArtifacts.get(artifactName);
            StubMapping stub = wireMockServer.getSingleStubMapping(stubId);
            wireMockServer.removeStub(stub);
            registeredArtifacts.remove(artifactName);
        }
    }

    private URI registerDownloadStub(String artifactName) {
        long artifactSize = getSize(artifactName);
        registeredArtifacts.computeIfAbsent(artifactName, key -> {
            UUID stubId = UUID.randomUUID();
            wireMockServer.stubFor(
                get("/" + key)
                    .withId(stubId)
                    .willReturn(
                        aResponse()
                            .withBodyFile(key)
                            .withHeader("Content-Length", "" + artifactSize))
                    .withPostServeAction(downloadListener.getName(), new ArtifactNameParameter(artifactName)));
            return stubId;
        });

        return httpServerBaseUri.resolve("/" + artifactName);
    }

    /**
     * Used for Jackson JSON processing only.
     */
    private static class ArtifactNameParameter {

        // Jackson requires this member to be accessible.
        @SuppressWarnings("unused")
        public String artifactName;

        public ArtifactNameParameter(@JsonProperty(DownloadListener.ARTIFACT_NAME_KEY) String artifactName) {
            this.artifactName = artifactName;
        }
    }
}
