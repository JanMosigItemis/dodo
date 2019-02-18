package de.itemis.jmo.dodo.tests.util;

import java.io.InputStream;
import java.net.URI;

import de.itemis.jmo.dodo.io.Downloader;

public class FakeDownloader implements Downloader {

    @Override
    public InputStream openStream(URI artifactUri) {
        return null;
    }
}
