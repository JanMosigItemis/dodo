package de.itemis.jmo.dodo.tests.util;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static de.itemis.jmo.dodo.tests.util.TestHelper.fail;
import static de.itemis.jmo.dodo.tests.util.TestHelper.printWarning;
import static java.util.Arrays.fill;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.itemis.jmo.dodo.util.DodoStallCallback;

/**
 * A "server" that provides arbitrary artifacts for download. Thought to be a replacement for a real
 * server, so that we do not need to download real artifacts from real servers of the Internet.
 */
public final class FakeServer {

    private final Map<String, UUID> registeredArtifacts = new HashMap<>();

    private URI httpServerBaseUri;
    private WireMockServer wireMockServer;
    private DownloadTrafficListener trafficListener;

    /**
     * Make the artifact with the specified name available for download.
     *
     * @return {@link URI} of the artifact.
     */
    public URI provide(String artifactName) {
        lazySetupHttpServer();
        unregisterDownloadStub(artifactName);
        return registerDownloadStub(artifactName);
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
            trafficListener = null;
            httpServerBaseUri = null;
            registeredArtifacts.clear();
        }
    }

    private void lazySetupHttpServer() {
        if (wireMockServer == null) {
            trafficListener = new DownloadTrafficListener();
            wireMockServer = new WireMockServer(options().dynamicPort().networkTrafficListener(trafficListener));

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

    public DodoStallCallback becomeStall(String artifactName, double percentage) {
        lazySetupHttpServer();

        unregisterDownloadStub(artifactName);

        UUID stubId = UUID.randomUUID();
        long artifactSize = getSize(artifactName);
        byte[] fakeDownload = new byte[(int) artifactSize / 2];
        fill(fakeDownload, (byte) 0);

        wireMockServer.stubFor(get("/" + artifactName)
            .withId(stubId)
            .willReturn(aResponse()
                .withBody(fakeDownload)
                .withHeader("Content-Length", "" + artifactSize)));
        registeredArtifacts.put(artifactName, stubId);

        DodoStallCallback callback = new DodoStallCallback();
        trafficListener.addCallback(callback);
        return callback;
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
        registeredArtifacts.computeIfAbsent(artifactName, key -> {
            UUID stubId = UUID.randomUUID();
            wireMockServer.stubFor(get("/" + key)
                .withId(stubId)
                .willReturn(aResponse()
                    .withBodyFile(key)));
            return stubId;
        });

        return httpServerBaseUri.resolve("/" + artifactName);
    }
}
