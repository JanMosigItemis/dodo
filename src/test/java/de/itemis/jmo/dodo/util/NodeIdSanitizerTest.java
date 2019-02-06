package de.itemis.jmo.dodo.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class NodeIdSanitizerTest {

    @Test
    public void dots_become_underscores() {
        String testStr = "one.two.three";
        String result = NodeIdSanitizer.sanitize(testStr);
        assertThat(result).isEqualTo("one_two_three");
    }
}
