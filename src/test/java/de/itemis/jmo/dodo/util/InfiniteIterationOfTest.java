package de.itemis.jmo.dodo.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InfiniteIterationOfTest {

    private static final int EXPECTED_VALUE = 8;

    private InfiniteIterationOf underTest;

    @BeforeEach
    public void setUp() {
        underTest = new InfiniteIterationOf(EXPECTED_VALUE);
    }

    @Test
    public void hasNext_returns_true() {
        assertThat(underTest.hasNext()).isTrue();
    }

    @Test
    public void next_returns_value_passed_to_constructor() {
        assertThat(underTest.next()).isEqualTo(EXPECTED_VALUE);
    }
}
