package de.itemis.jmo.dodo.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.validation.ChecksumValidator;

public class DownloadScriptTest {

    private DodoDownload downloadMock;
    private Supplier<DodoDownload> downloadSup;
    private ChecksumValidator hashCodeValidatorMock;
    private Supplier<ChecksumValidator> hashCodeValidatorSup;

    private DownloadScript underTest;

    @BeforeEach
    public void setUp() {
        downloadMock = mock(DodoDownload.class);
        downloadSup = () -> downloadMock;
        hashCodeValidatorMock = mock(ChecksumValidator.class);
        hashCodeValidatorSup = () -> hashCodeValidatorMock;

        underTest = new DownloadScript(downloadSup, hashCodeValidatorSup);
    }

    @Test
    public void createDownload_calls_download_create_function() {
        var result = underTest.createDownload();

        assertThat(result).as("result of method").isEqualTo(downloadMock);
    }

    @Test
    public void createHashValidator_calls_validator_factory_with_specified_algorithm_name() {
        var result = underTest.createHashCodeValidator();

        assertThat(result).as("result of method").isEqualTo(hashCodeValidatorMock);
    }
}
