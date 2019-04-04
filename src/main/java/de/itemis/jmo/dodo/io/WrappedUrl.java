package de.itemis.jmo.dodo.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * In some test cases it is necessary to mock a {@link URL}. Since this class is declared final in
 * the JDK, it cannot be mocked easily. Thus, instead of directly using instances of {@link URL}, we
 * are going to use instances of this wrapper instead. They can be easily mocked and they are
 * thought to provide only those methods of {@link URL} that are required for DoDo.
 */
public class WrappedUrl {

    private final URL url;

    /**
     * Create a new instance.
     *
     * @param url - Wrap this {@link URL}.
     */
    public WrappedUrl(URL url) {
        this.url = url;
    }

    /**
     * Delegates to {@link URL#openStream()}.
     *
     * @return an {@link InputStream}.
     * @throws IOException
     */
    public InputStream openStream() throws IOException {
        URLConnection openConnection = url.openConnection();
        openConnection.setReadTimeout(3000);
        return openConnection.getInputStream();
    }

    /**
     * Like {@link URLConnection#getContentLength()}
     */
    public long getContentLength() {
        long result = -1;
        try {
            result = url.openConnection().getContentLengthLong();
        } catch (IOException | IllegalArgumentException e) {
            throw new DodoException("Could not open connection.", e);
        }

        return result;
    }
}
