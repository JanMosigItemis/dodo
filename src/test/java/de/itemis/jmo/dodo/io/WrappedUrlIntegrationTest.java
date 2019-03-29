package de.itemis.jmo.dodo.io;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

import de.itemis.jmo.dodo.tests.util.FakeServer;

public class WrappedUrlIntegrationTest {

    private static final String ARTIFACT = "test.artifact.one";

    private static FakeServer FAKE_SERVER;
    private static URL TEST_URL;

    private WrappedUrl underTest;

    @BeforeEach
    public void setUp() {
        underTest = new WrappedUrl(TEST_URL);
    }

    @BeforeAll
    public static void setUpStatic() throws Exception {
        FAKE_SERVER = new FakeServer();
        TEST_URL = FAKE_SERVER.provide(ARTIFACT).toURL();
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
}
