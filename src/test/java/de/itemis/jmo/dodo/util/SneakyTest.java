package de.itemis.jmo.dodo.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class SneakyTest {
    private static final String EXPECTED_CHECKED_EXCEPTION_MSG = "Expected checked exception";
    private static final String EXPECTED_UNCHECKED_EXCEPTION_MSG = "Expected unchecked exception";
    private static final RuntimeException EXPECTED_UNCHECKED_EXCEPTION = new RuntimeException(EXPECTED_UNCHECKED_EXCEPTION_MSG);
    private static final Exception EXPECTED_CHECKED_EXCEPTION = new Exception(EXPECTED_CHECKED_EXCEPTION_MSG);

    @Test
    public void throwThat_throws_Unchecked_Exceptions() {
        assertThatThrownBy(() -> Sneaky.throwThat(EXPECTED_UNCHECKED_EXCEPTION)).isExactlyInstanceOf(EXPECTED_UNCHECKED_EXCEPTION.getClass())
            .hasMessage(EXPECTED_UNCHECKED_EXCEPTION.getMessage());
    }

    @Test
    public void throwThat_throws_Checked_Exceptions() {
        assertThatThrownBy(() -> Sneaky.throwThat(EXPECTED_CHECKED_EXCEPTION)).isExactlyInstanceOf(EXPECTED_CHECKED_EXCEPTION.getClass())
            .hasMessage(EXPECTED_CHECKED_EXCEPTION.getMessage());
    }
}
