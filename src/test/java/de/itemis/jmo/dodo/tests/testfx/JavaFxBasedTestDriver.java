package de.itemis.jmo.dodo.tests.testfx;

import static de.itemis.jmo.dodo.tests.util.TestHelper.fail;
import static de.itemis.jmo.dodo.tests.util.TestHelper.printWarning;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import org.testfx.framework.junit5.ApplicationTest;

/**
 * Base class for TestFX based UI tests. Aims at providing a unified intialization base for all
 * TestFX based UI tests.
 *
 * @author mosig_user
 *
 */
public abstract class JavaFxBasedTestDriver extends ApplicationTest implements DodoTestsJunit5Lifecycle {

    @Override
    public void beforeEach() {
        try {
            internalBefore();
            waitForFxEvents();
        } catch (Exception e) {
            fail("Could not init test runner.", e);
        }
    }

    @Override
    public void afterEach() {
        try {
            waitForFxEvents();
            internalAfter();
        } catch (Exception e) {
            printWarning("Test runner cleanup failed.", e);
        }
    }
}