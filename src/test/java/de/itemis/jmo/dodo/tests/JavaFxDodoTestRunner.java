/**
 *
 */
package de.itemis.jmo.dodo.tests;

import static de.itemis.jmo.dodo.tests.TestHelper.fail;
import static de.itemis.jmo.dodo.tests.TestHelper.printWarning;

import org.testfx.framework.junit5.ApplicationTest;

import java.net.URI;

import de.itemis.jmo.dodo.DodoApp;
import javafx.stage.Stage;

/**
 * A "driver" for UI tests. It decouples the actual test code ("what to test") from the knowledge on
 * how to actually perform those tests. This helps keeping the tests compiling in case test
 * technology or app technology changes.
 */
public class JavaFxDodoTestRunner extends ApplicationTest implements DodoTestRunner {

    @Override
    public void beforeEach() {
        try {
            internalBefore();
        } catch (Exception e) {
            fail("Could not init test runner.", e);
        }
    }

    @Override
    public void afterEach() {
        try {
            internalAfter();
        } catch (Exception e) {
            printWarning("Test runner cleanup failed.", e);
        }
    }

    @Override
    public void start(Stage testStage) throws Exception {
        var dodo = new DodoApp();
        dodo.start(testStage);
    }

    @Override
    public void addDownloadSource(String artifactName, URI artifactUri) {
        // TODO Auto-generated method stub

    }

    @Override
    public void download(String artifactName) {
        // TODO Auto-generated method stub

    }

    @Override
    public void assertDownloadSuccessIndicated(String artifactName) {
        // TODO Auto-generated method stub

    }
}
