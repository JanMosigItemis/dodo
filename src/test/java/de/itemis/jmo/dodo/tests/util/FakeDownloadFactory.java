package de.itemis.jmo.dodo.tests.util;

import java.net.URI;

import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.io.DownloadFactory;

/**
 * This factory does create mocked download objects. Supposed to be used in tests only.
 */
public class FakeDownloadFactory implements DownloadFactory {

    @Override
    public DodoDownload createDownload(URI uri) {
        return null;
    }
}
