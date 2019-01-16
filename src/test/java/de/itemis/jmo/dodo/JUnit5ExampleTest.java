package de.itemis.jmo.dodo;

import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

public class JUnit5ExampleTest {

    @Test
    public void justAnExample() {
        System.out.println("Hello World!");
    }

    @Test
    public void thisTestFails() {
        fail("Failing intentionally.");
    }
}
