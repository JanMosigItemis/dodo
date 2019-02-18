package de.itemis.jmo.dodo.parsing;

import static de.itemis.jmo.dodo.tests.util.JsonTestHelper.toDoubleQuotes;
import static de.itemis.jmo.dodo.tests.util.UriTestData.VALID_URI;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.tests.util.FakeDownloader;

public class JsonScriptParserTest {

    private FakeDownloader fakeDownloader;

    private JsonScriptParser underTest;

    @BeforeEach
    public void setUp() {
        fakeDownloader = new FakeDownloader();

        underTest = new JsonScriptParser(fakeDownloader);
    }

    @Test
    public void parse_validJson_returns_new_script() {
        DownloadScript result = underTest.parse(toDoubleQuotes("{'uri':'" + VALID_URI + "'}"));
        assertThat(result).isNotNull();
    }
}
