/**
 *
 */
package de.itemis.jmo.dodo.io;

import static com.google.common.base.Strings.nullToEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * A {@link DownloadFactory} that can handle typical Internet download protocols, e. g. HTTP and
 * HTTPS.
 */
public class InternetDownloadFactory implements DownloadFactory {

    private static final Logger LOG = LoggerFactory.getLogger(InternetDownloadFactory.class);

    private final Map<String, Function<WrappedUrl, DodoDownload>> ACCEPTED_INTERNET_PROTOCOLS = new HashMap<>();

    /**
     * Create a new instance.
     *
     * @param downloadBlockSizeStrategy - Created {@link DodoDownload downloads} will use this
     *        strategy in order to select the block size of one read operation.
     */
    public InternetDownloadFactory(Iterator<Integer> downloadBlockSizeStrategy) {
        ACCEPTED_INTERNET_PROTOCOLS.put("http", url -> new HttpDownload(url, downloadBlockSizeStrategy));
        ACCEPTED_INTERNET_PROTOCOLS.put("https", url -> new HttpDownload(url, downloadBlockSizeStrategy));
    }

    @Override
    public DodoDownload createDownload(URI uri) {
        String scheme = nullToEmpty(uri.getScheme());
        WrappedUrl downloadUrl = null;

        if (!scheme.isBlank()) {
            try {
                downloadUrl = new WrappedUrl(uri.toURL());
            } catch (MalformedURLException e) {
                if (e.getMessage().contains("unknown protocol")) {
                    LOG.warn("While creating download: Encountered unknown protocol: '" + scheme + "'.");
                } else {
                    throw new DodoException("Provided URI was not a valid URL.", e);
                }
            }
        }

        return ACCEPTED_INTERNET_PROTOCOLS.getOrDefault(scheme, unused -> new NopDownload()).apply(downloadUrl);
    }
}
