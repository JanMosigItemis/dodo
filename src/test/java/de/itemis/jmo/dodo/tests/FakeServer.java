package de.itemis.jmo.dodo.tests;

import static de.itemis.jmo.dodo.tests.IoHelperForTests.deleteRecursively;
import static de.itemis.jmo.dodo.tests.IoHelperForTests.readBytes;
import static de.itemis.jmo.dodo.tests.TestHelper.fail;
import static de.itemis.jmo.dodo.tests.TestHelper.printWarning;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 * A "server" that provides arbitrary artifacts for download. Thought to be a replacement for a real
 * server, so that we do not need to download real artifacts from real servers of the Internet.
 */
public final class FakeServer {

    private final Map<String, byte[]> artifact = new HashMap<>();

    private Server httpServer;
    private URI httpServerBaseUri;
    private Path resourceBasePath;

    /**
     * Make the artifact with the specified name available for download.
     *
     * @return {@link URI} of the artifact.
     */
    public URI provide(String artifactName) {
        lazyCreateResourceBase();
        lazySetupHttpServer();

        byte[] artifactBytes = artifact.computeIfAbsent(artifactName, key -> readBytes(this.getClass(), key));
        try {
            Files.write(resourceBasePath.resolve(artifactName), artifactBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            fail("Could not write artifact file.", e);
        }

        return httpServerBaseUri.resolve(artifactName);
    }

    /**
     * Stop the server and delete all resources.
     */
    public void stop() {
        try {
            if (httpServer != null) {
                httpServer.stop();
            }
            deleteRecursively(resourceBasePath);
        } catch (Exception e) {
            printWarning("Stopping fake server ran into a problem", e);
        } finally {
            httpServer = null;
            httpServerBaseUri = null;
            resourceBasePath = null;
            artifact.clear();
        }
    }

    private void lazyCreateResourceBase() {
        if (resourceBasePath == null) {
            try {
                resourceBasePath = Files.createTempDirectory(this.getClass().getSimpleName());
            } catch (IOException e) {
                fail("Could not create temporary base dir for HTTP server.", e);
            }
        }
    }

    private void lazySetupHttpServer() {
        if (httpServer == null) {
            httpServer = new Server();
            ServerConnector connector = new ServerConnector(httpServer);
            connector.setPort(0);
            Connector[] connectors = new Connector[] {connector};
            httpServer.setConnectors(connectors);

            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setResourceBase(resourceBasePath.toString());
            httpServer.setHandler(resourceHandler);

            try {
                httpServer.start();
            } catch (Exception e) {
                fail("Could not start test HTTP server.", e);
            }
            httpServerBaseUri = URI.create("http://localhost:" + connector.getLocalPort());
        }
    }

}
