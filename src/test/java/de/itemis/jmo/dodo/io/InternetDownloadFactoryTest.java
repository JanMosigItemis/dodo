package de.itemis.jmo.dodo.io;

import static de.itemis.jmo.dodo.tests.util.UriTestData.VALID_URI_NO_SCHEME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;

import de.itemis.jmo.dodo.error.DodoException;
import de.itemis.jmo.dodo.tests.util.UriTestData;

public class InternetDownloadFactoryTest {

    private InternetDownloadFactory underTest;

    @BeforeEach
    public void setUp() {
        underTest = new InternetDownloadFactory();
    }

    @Test
    public void createDownload_creates_something() {
        DodoDownload result = underTest.createDownload(VALID_URI_NO_SCHEME);

        assertThat(result).as("method result").isNotNull();
    }

    @Test
    public void creates_http_download_for_http_protocol() {
        DodoDownload result = underTest.createDownload(URI.create("http://some.uri"));

        assertThat(result).isInstanceOf(HttpDownload.class);
    }

    @Test
    public void creates_http_download_for_https_protocol() {
        DodoDownload result = underTest.createDownload(URI.create("https://some.uri"));

        assertThat(result).isInstanceOf(HttpDownload.class);
    }

    @Test
    public void creates_nop_download_for_unknown_protocol() {
        DodoDownload result = underTest.createDownload(URI.create("unknown://some.uri"));

        assertThat(result).isInstanceOf(NopDownload.class);
    }

    @Test
    public void creates_nop_download_if_no_scheme() {
        DodoDownload result = underTest.createDownload(URI.create("some.uri"));

        assertThat(result).isInstanceOf(NopDownload.class);
    }

    @Test
    public void throws_dodoexception_if_uri_is_no_url() {
        assertThatThrownBy(() -> underTest.createDownload(UriTestData.URI_BUT_NO_URL))
            .isInstanceOf(DodoException.class)
            .hasMessage("Provided URI was not a valid URL.")
            .hasCauseInstanceOf(MalformedURLException.class);
    }

}
