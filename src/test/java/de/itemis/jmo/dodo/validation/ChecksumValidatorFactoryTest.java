package de.itemis.jmo.dodo.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import de.itemis.jmo.dodo.tests.util.Console;

public class ChecksumValidatorFactoryTest {

    private static final byte[] EXPECTED_CHECKSUM = new byte[0];

    @RegisterExtension
    Console console = new Console();

    private ChecksumValidatorFactory underTest;

    @BeforeEach
    public void setUp() {
        underTest = new ChecksumValidatorFactory();
    }

    @Test
    public void apply_creates_md5_validator() {
        var result = underTest.apply(EXPECTED_CHECKSUM, "MD5");

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(Md5ChecksumValidator.class);
    }

    @Test
    public void apply_creates_sha1_validator() {
        var result = underTest.apply(EXPECTED_CHECKSUM, "SHA-1");

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(Sha1ChecksumValidator.class);
    }

    @Test
    public void apply_creates_sha256_validator() {
        var result = underTest.apply(EXPECTED_CHECKSUM, "SHA-256");

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(Sha256ChecksumValidator.class);
    }

    @Test
    public void apply_creates_sha512_validator() {
        var result = underTest.apply(EXPECTED_CHECKSUM, "SHA-512");

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(Sha512ChecksumValidator.class);
    }

    @Test
    public void unknown_algorithm_creates_NopValidator() {
        var unknownAlgorithmName = "unknownAlgo";
        var result = underTest.apply(EXPECTED_CHECKSUM, unknownAlgorithmName);

        assertThat(result).as("Encountered unexpected return type.").isInstanceOf(NopChecksumValidator.class);
        console.assertAnyLineContains("Encountered unknown hash code algorithm name '" + unknownAlgorithmName + "'");
    }
}
