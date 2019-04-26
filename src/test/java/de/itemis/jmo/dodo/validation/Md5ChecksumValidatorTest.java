package de.itemis.jmo.dodo.validation;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.DODO_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.io.BaseEncoding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.tests.util.ExpectedExceptions;

public class Md5ChecksumValidatorTest {

    private static final byte[] END_OF_SOURCE = new byte[0];

    private static final byte[] EXPECTED_CHECKSUM = BaseEncoding.base16().decode("ed076287532e86365e841e92bfc50d8c".toUpperCase());

    private DataSource goodDataMock;
    private DataSource badDataMock;

    private Md5ChecksumValidator underTest;

    @BeforeEach
    public void setUp() {
        goodDataMock = mock(DataSource.class);
        when(goodDataMock.read()).thenReturn("Hello World!".getBytes(StandardCharsets.UTF_8), END_OF_SOURCE);

        badDataMock = mock(DataSource.class);
        when(badDataMock.read()).thenReturn("Bad Data!".getBytes(StandardCharsets.UTF_8), END_OF_SOURCE);

        underTest = new Md5ChecksumValidator(EXPECTED_CHECKSUM);
    }

    @Test
    public void validate_success() {
        assertThat(underTest.verify(goodDataMock)).as("Correct hash code must yield true").isTrue();
    }

    @Test
    public void validate_fail() {
        assertThat(underTest.verify(badDataMock)).as("Incorrect hash code must yield false").isFalse();
    }

    @Test
    public void when_data_read_fails_exception_is_propagated_as_is() {
        doThrow(DODO_EXCEPTION).when(goodDataMock).read();

        ExpectedExceptions.assertThatThrownBy(() -> underTest.verify(goodDataMock), DODO_EXCEPTION);
    }

    @Test
    public void when_array_passed_to_constructor_is_changed_then_changes_are_not_reflected_withing_validator_instance() {
        EXPECTED_CHECKSUM[0] = 123;
        assertThat(underTest.verify(goodDataMock)).as("Changes in expected checksum must not be reflected into the validator instance.").isTrue();
    }
}
