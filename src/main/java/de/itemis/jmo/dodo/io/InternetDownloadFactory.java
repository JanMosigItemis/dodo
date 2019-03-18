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
import java.util.Map;
import java.util.function.Function;

import de.itemis.jmo.dodo.error.DodoException;

/**
 * A {@link DownloadFactory} that can handle typical Internet download protocols, e. g. HTTP and
 * HTTPS.
 */
public class InternetDownloadFactory implements DownloadFactory {

    private static final Logger LOG = LoggerFactory.getLogger(InternetDownloadFactory.class);

    private static final Map<String, Function<UrlWrapper, DodoDownload>> ACCEPTED_INTERNET_PROTOCOLS = new HashMap<>();
    static {
        ACCEPTED_INTERNET_PROTOCOLS.put("http", url -> new HttpDownload(url));
        ACCEPTED_INTERNET_PROTOCOLS.put("https", url -> new HttpDownload(url));
    }

    @Override
    public DodoDownload createDownload(URI uri) {
        String scheme = nullToEmpty(uri.getScheme());
        UrlWrapper downloadUrl = null;

        if (!scheme.isBlank()) {
            try {
                downloadUrl = new UrlWrapper(uri.toURL());
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
