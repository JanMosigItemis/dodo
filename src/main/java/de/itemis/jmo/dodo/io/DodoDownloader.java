/**
 *
 */
package de.itemis.jmo.dodo.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * Default implementation of a class that can open input streams from URIs.
 *
 */
public class DodoDownloader implements Downloader {

    @Override
    public InputStream openStream(URI artifactUri) {
        InputStream result = null;
        try {
            result = artifactUri.toURL().openStream();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }
}
