package de.itemis.jmo.dodo.parsing;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;

import de.itemis.jmo.dodo.io.Downloader;
import de.itemis.jmo.dodo.model.DownloadScript;

/**
 * A JSON based implementation of a {@link StringParser}. Understands JSON only.
 */
public class JsonScriptParser implements StringParser<DownloadScript> {

    private final Downloader downloader;

    /**
     * Create a new instance.
     *
     * @param downloader - Created {@link DownloadScript} entities will use this {@link Downloader}
     *        in order to perform the actual download.
     */
    public JsonScriptParser(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public DownloadScript parse(String text) {
        JSONParser jsonParser = new JSONParser();
        JSONObject object = null;
        try {
            object = (JSONObject) jsonParser.parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new DownloadScript(URI.create(object.get("uri").toString()), downloader);
    }
}
