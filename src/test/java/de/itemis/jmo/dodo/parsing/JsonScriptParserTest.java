package de.itemis.jmo.dodo.parsing;

import static de.itemis.jmo.dodo.tests.util.JsonTestHelper.toDoubleQuotes;
import static de.itemis.jmo.dodo.tests.util.UriTestData.OTHER_VALID_URI_NO_SCHEME;
import static de.itemis.jmo.dodo.tests.util.UriTestData.VALID_URI_NO_SCHEME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.model.DownloadScript;
import de.itemis.jmo.dodo.validation.ChecksumValidator;

public class JsonScriptParserTest {

    private static final String HASH_ALGORITHM = "hashAlgorithm";

    private Function<URI, DodoDownload> downloadFactoryMock;
    private BiFunction<Supplier<DodoDownload>, String, ChecksumValidator> validatorFactoryMock;

    private JsonScriptParser underTest;

    // mock API does not go well with generics. Everything safe here.
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        downloadFactoryMock = mock(Function.class);
        validatorFactoryMock = mock(BiFunction.class);

        underTest = new JsonScriptParser(downloadFactoryMock, validatorFactoryMock);
    }

    @Test
    public void parse_validJson_returns_new_script() {
        var result = parseOp().get();
        assertThat(result).isNotNull();
    }

    @Test
    public void createDownload_passes_uri_to_download_factory() {
        var result = parseOp().get();
        result.createDownload();

        verify(downloadFactoryMock).apply(eq(VALID_URI_NO_SCHEME));
    }

    @Test
    public void createValidator_passes_algorithmName_to_validator_factory() {
        var result = parseOp().get();
        result.createHashCodeValidator();

        verify(validatorFactoryMock).apply(any(), eq(HASH_ALGORITHM));
    }

    @Test
    public void createValidator_passes_hashUri_to_downloadFactory_factory_and_this_factory_is_used_during_validator_creation() {
        invokeDownloadSupWhenValidatorFactoryIsInvoked();

        var result = parseOp().get();
        result.createHashCodeValidator();

        verify(downloadFactoryMock).apply(OTHER_VALID_URI_NO_SCHEME);
    }

    /*
     * ##########################
     *
     * Start private helper code.
     *
     * ##########################
     */

    private Supplier<DownloadScript> parseOp() {
        return parseOp(
            toDoubleQuotes("{'uri':'" + VALID_URI_NO_SCHEME + "', 'hashUri':'" + OTHER_VALID_URI_NO_SCHEME + "', 'hashAlgorithm':'" + HASH_ALGORITHM + "'}"));
    }

    private Supplier<DownloadScript> parseOp(String json) {
        return () -> underTest.parse(json);
    }

    // Mockito's API does not go well with generics. "Cast" is safe.
    @SuppressWarnings("unchecked")
    private void invokeDownloadSupWhenValidatorFactoryIsInvoked() {
        doAnswer(invocation -> {
            Supplier<?> downloadSup = invocation.getArgument(0);
            downloadSup.get();
            return null;
        }).when(validatorFactoryMock).apply(any(Supplier.class), anyString());
    }
}
