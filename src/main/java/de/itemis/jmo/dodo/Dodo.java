package de.itemis.jmo.dodo;

import javafx.application.Application;

/**
 * <p>
 * Main entry point for the application. Delegates to {@link DodoApp}, because we cannot directly
 * inherit from {@link Application} here. This is, because the JavaFX runtime is not part of the JDK
 * since release 11 anymore and the JVM launcher would prevent launching the app if the main class
 * would extend from {@link Application}.
 * </p>
 *
 * @see <a href=
 *      "https://github.com/javafxports/openjdk-jfx/issues/236">https://github.com/javafxports/openjdk-jfx/issues/236</a>
 *      for details.
 */
public class Dodo {

    public static void main(String[] args) {
        Application.launch(DodoApp.class, args);
    }
}
