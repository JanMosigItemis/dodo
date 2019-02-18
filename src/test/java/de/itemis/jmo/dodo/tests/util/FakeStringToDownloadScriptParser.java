package de.itemis.jmo.dodo.tests.util;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.RUNTIME_EXCEPTION;

import java.net.URI;

import de.itemis.jmo.dodo.io.Downloader;
import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.parsing.StringParser;

/**
 * <p>
 * A parser that does parse all {@link String Strings} to the same {@link DownloadScript} entity or
 * fails with defined invalid {@link String Strings}.
 * </p>
 * <p>
 * Thought to be used in tests where such a parser is necessary, but does not need to do real
 * parsing.
 * </p>
 */
public class FakeStringToDownloadScriptParser implements StringParser<DownloadScript> {

    private final String invalidScript;
    private final Downloader fakeDownloader;

    /**
     * Create a new instance.
     *
     * @param invalidScript - Will result in a parse error.
     */
    public FakeStringToDownloadScriptParser(String invalidScript, Downloader fakeDownloader) {
        this.invalidScript = invalidScript;
        this.fakeDownloader = fakeDownloader;
    }

    @Override
    public DownloadScript parse(String text) {
        DownloadScript result = new DownloadScript(URI.create("artifactUri"), fakeDownloader);
        if (invalidScript.equals(text)) {
            throw RUNTIME_EXCEPTION;
        }

        return result;
    }
}
