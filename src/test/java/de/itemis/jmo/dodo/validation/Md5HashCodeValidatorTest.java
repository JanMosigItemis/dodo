package de.itemis.jmo.dodo.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.io.BaseEncoding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.io.DodoDownload;

public class Md5HashCodeValidatorTest {

    private static final byte[] END_OF_SOURCE = new byte[0];

    private DataSource dataMock;
    private DataSource expectedHashCodeMock;
    private Supplier<DodoDownload> hashSupMock;
    private DodoDownload hashCodeDownloadMock;

    private Md5HashCodeValidator underTest;

    // Mockito and generics.
    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        dataMock = mock(DataSource.class);
        when(dataMock.read()).thenReturn("Hello World!".getBytes(StandardCharsets.UTF_8), END_OF_SOURCE);

        expectedHashCodeMock = mock(DataSource.class);
        hashCodeDownloadMock = mock(DodoDownload.class);
        hashSupMock = mock(Supplier.class);
        when(hashSupMock.get()).thenReturn(hashCodeDownloadMock);
        when(hashCodeDownloadMock.getDataSource()).thenReturn(expectedHashCodeMock);
        byte[] expectedhashCodeBytes = BaseEncoding.base16().decode("ed076287532e86365e841e92bfc50d8c".toUpperCase());
        when(expectedHashCodeMock.read()).thenReturn(expectedhashCodeBytes, END_OF_SOURCE);

        underTest = new Md5HashCodeValidator(hashSupMock);
    }

    @Test
    public void validate_success() {
        assertThat(underTest.verify(dataMock)).as("Correct hash code must yield true").isTrue();
    }
}
