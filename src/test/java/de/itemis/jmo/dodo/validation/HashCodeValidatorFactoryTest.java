package de.itemis.jmo.dodo.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;
import de.itemis.jmo.dodo.tests.util.Console;

public class HashCodeValidatorFactoryTest {

    @RegisterExtension
    Console console = new Console();

    private Supplier<DodoDownload> downloadSupMock;

    private HashCodeValidatorFactory underTest;

    // Mockito and generics.
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        downloadSupMock = Mockito.mock(Supplier.class);

        underTest = new HashCodeValidatorFactory();
    }

    @Test
    public void apply_creates_md5_validator() {
        var result = underTest.apply(downloadSupMock, "MD5");

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(Md5HashCodeValidator.class);
    }

    @Test
    public void apply_creates_sha1_validator() {
        var result = underTest.apply(downloadSupMock, "SHA-1");

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(Sha1HashCodeValidator.class);
    }

    @Test
    public void apply_creates_sha256_validator() {
        var result = underTest.apply(downloadSupMock, "SHA-256");

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(Sha256HashCodeValidator.class);
    }

    @Test
    public void apply_creates_sha512_validator() {
        var result = underTest.apply(downloadSupMock, "SHA-512");

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(Sha512HashCodeValidator.class);
    }

    @Test
    public void unknown_algorithm_creates_NopValidator() {
        var unknownAlgorithmName = "unknownAlgo";
        var result = underTest.apply(downloadSupMock, unknownAlgorithmName);

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(NopHashCodeValidator.class);
        console.anyLineContains("Encountered unknown hash code algorithm name '" + unknownAlgorithmName + "'");
    }
}
