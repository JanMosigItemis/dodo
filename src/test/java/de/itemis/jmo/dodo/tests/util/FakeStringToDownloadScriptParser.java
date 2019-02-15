package de.itemis.jmo.dodo.tests.util;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.RUNTIME_EXCEPTION;

import java.net.URI;

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

    /**
     * Create a new instance.
     *
     * @param invalidScript - Will result in a parse error.
     */
    public FakeStringToDownloadScriptParser(String invalidScript) {
        this.invalidScript = invalidScript;
    }

    @Override
    public DownloadScript parse(String text) {
        DownloadScript result = new DownloadScript(URI.create("artifactUri"));
        if (invalidScript.equals(text)) {
            throw RUNTIME_EXCEPTION;
        }

        return result;
    }
}
