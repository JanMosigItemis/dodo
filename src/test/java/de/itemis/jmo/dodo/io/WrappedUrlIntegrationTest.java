package de.itemis.jmo.dodo.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Duration.FIVE_SECONDS;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;

import de.itemis.jmo.dodo.tests.util.DownloadBlockSizeStrategyForTests;
import de.itemis.jmo.dodo.tests.util.FakeServer;

public class WrappedUrlIntegrationTest {

    private static final String ARTIFACT = "test.artifact.one";

    private static FakeServer FAKE_SERVER;
    private static URL TEST_URL;

    private WrappedUrl underTest;

    @BeforeEach
    public void setUp() throws Exception {
        TEST_URL = FAKE_SERVER.provide(ARTIFACT).toURL();
        underTest = new WrappedUrl(TEST_URL);
    }

    @BeforeAll
    public static void setUpStatic() {
        FAKE_SERVER = new FakeServer(new DownloadBlockSizeStrategyForTests());
    }

    @AfterAll
    public static void tearDownStatic() {
        if (FAKE_SERVER != null) {
            FAKE_SERVER.stop();
        }
    }

    @Test
    public void getContentLength_returns_correct_content_length() {
        long expectedResult = FAKE_SERVER.getSize(ARTIFACT);
        long actualResult = underTest.getContentLength();

        assertThat(actualResult).as("Encountered unexpected content length.").isEqualTo(expectedResult);
    }

    @Test
    public void openStream_sets_read_timeout_appropriate_value() {
        FAKE_SERVER.becomeStall(ARTIFACT, 50);

        await().atMost(FIVE_SECONDS).alias("Wait for read timeout within specified time").untilAsserted(() -> {
            try (InputStream stream = underTest.openStream()) {
                assertThatThrownBy(() -> stream.readAllBytes()).isInstanceOf(SocketTimeoutException.class).hasMessage("Read timed out");
            }
        });
    }
}
