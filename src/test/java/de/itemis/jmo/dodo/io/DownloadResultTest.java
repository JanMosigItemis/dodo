package de.itemis.jmo.dodo.io;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DownloadResultTest {

    @Test
    public void hashCode_validation_result_false() {
        var underTest = new DownloadResult(false);

        assertThat(underTest.hashIsValid()).isFalse();
    }

    @Test
    public void hashCode_validation_result_true() {
        var underTest = new DownloadResult(true);

        assertThat(underTest.hashIsValid()).isTrue();
    }
}
