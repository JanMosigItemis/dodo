package de.itemis.jmo.dodo.ui;

import static com.google.common.base.Strings.nullToEmpty;

import com.google.common.base.StandardSystemProperty;

import de.itemis.jmo.dodo.util.InstantiationNotAllowedException;

/**
 * <p>
 * The currently used version of OpenJFX (11.0.2) has a bug when run in headless mode with Monocle
 * on Windows machines. A fix will not be available before release 13.x. However, there exists a
 * workaround.
 * </p>
 * <p>
 * Run {@link #runIfOnWindows()} before any JavaFX code is run or initialized, e. g.:
 *
 * <pre>
 * package a.b.c
 *
 * public class SomeMain {
 *     static {
 *         OpenJfxMonocleWindowsBugWorkaround.runIfOnWindows();
 *     }
 *
 *     public static void main(String[] args) {
 *         // Run FX App
 *     }
 * }
 * </pre>
 *
 * @see <a href="https://github.com/javafxports/openjdk-jfx">OpenJFX</a>
 * @see <a href="https://wiki.openjdk.java.net/display/OpenJFX/Monocle">Monocle</a>
 * @see <a href=
 *      "https://github.com/javafxports/openjdk-jfx/issues/66#issuecomment-468370664">Workaround</a>
 */
final class OpenJfxMonocleWindowsBugWorkaround {

    private static boolean alreadyLoaded = false;

    private OpenJfxMonocleWindowsBugWorkaround() {
        throw new InstantiationNotAllowedException();
    }

    /**
     * Check if we are running on a Windows OS and perform workaround if so.
     *
     * @see <a href=
     *      "https://github.com/javafxports/openjdk-jfx/issues/66#issuecomment-468370664">Workaround</a>
     */
    public static void runIfOnWindows() {
        if (!alreadyLoaded && osIsWindows()) {
            alreadyLoaded = true;
            System.load("C:\\Windows\\System32\\WindowsCodecs.dll");
        }
    }

    private static boolean osIsWindows() {
        return nullToEmpty(System.getProperty(StandardSystemProperty.OS_NAME.key())).toLowerCase().contains("windows");
    }
}
