package de.itemis.jmo.dodo.parsing;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;

import de.itemis.jmo.dodo.io.DownloadFactory;
import de.itemis.jmo.dodo.model.DownloadScript;

/**
 * A JSON based implementation of a {@link StringParser}. Understands JSON only.
 */
public class JsonScriptParser implements StringParser<DownloadScript> {

    private final DownloadFactory downloadFactory;

    /**
     * Create a new instance.
     *
     * @param downloadFactory - Created {@link DownloadScript} entities will use this
     *        {@link DownloadFactory} in order to handle downloads.
     */
    public JsonScriptParser(DownloadFactory downloadFactory) {
        this.downloadFactory = downloadFactory;
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

        URI downloadUri = URI.create(object.get("uri").toString());
        return new DownloadScript(() -> downloadFactory.createDownload(downloadUri));
    }
}
