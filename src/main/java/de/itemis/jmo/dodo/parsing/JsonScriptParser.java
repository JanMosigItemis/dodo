package de.itemis.jmo.dodo.parsing;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.validation.HashCodeValidator;

/**
 * A JSON based implementation of a {@link StringParser}. Understands JSON only.
 */
public class JsonScriptParser implements StringParser<DownloadScript> {

    private final Function<URI, DodoDownload> downloadFactory;
    private final BiFunction<Supplier<DodoDownload>, String, HashCodeValidator> validatorFactory;

    /**
     * Create a new instance.
     *
     * @param downloadFactory - Used by the created {@link DownloadScript} instances for creating
     *        downloads.
     * @param validatorFactory - Used by the created {@link DownloadScript} instances for creating
     *        download hash code validators.
     */
    public JsonScriptParser(Function<URI, DodoDownload> downloadFactory, BiFunction<Supplier<DodoDownload>, String, HashCodeValidator> validatorFactory) {
        this.downloadFactory = downloadFactory;
        this.validatorFactory = validatorFactory;
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
        String algorithmName = object.get("hashAlgorithm").toString();
        URI hashCodeUri = URI.create(object.get("hashUri").toString());

        return new DownloadScript(() -> downloadFactory.apply(downloadUri),
            () -> validatorFactory.apply(() -> downloadFactory.apply(hashCodeUri), algorithmName));
    }
}
