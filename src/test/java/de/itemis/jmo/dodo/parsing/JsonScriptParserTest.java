package de.itemis.jmo.dodo.parsing;

import static de.itemis.jmo.dodo.tests.util.JsonTestHelper.toDoubleQuotes;
import static de.itemis.jmo.dodo.tests.util.UriTestData.VALID_URI_NO_SCHEME;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.tests.util.FakeDownloadFactory;

public class JsonScriptParserTest {

    private FakeDownloadFactory fakeDownloader;

    private JsonScriptParser underTest;

    @BeforeEach
    public void setUp() {
        fakeDownloader = new FakeDownloadFactory();

        underTest = new JsonScriptParser(fakeDownloader);
    }

    @Test
    public void parse_validJson_returns_new_script() {
        DownloadScript result = underTest.parse(toDoubleQuotes("{'uri':'" + VALID_URI_NO_SCHEME + "'}"));
        assertThat(result).isNotNull();
    }
}
