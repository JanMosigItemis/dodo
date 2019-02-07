package de.itemis.jmo.dodo.tests.util;

import static de.itemis.jmo.dodo.tests.util.TestHelper.fail;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/**
 * <p>
 * General I/O related convenience methods that make test implementation easier.
 * </p>
 * <p>
 * Note that all methods will throw an {@link AssertionError} in case something goes wrong.
 * </p>
 */
public final class IoHelperForTests {

    private IoHelperForTests() {
        throw new UnsupportedOperationException("Instantiation is not supported");
    }

    /**
     * <p>
     * Create a new {@link InputStream} of {@code resourceName} using {@code clientClass's} class
     * loader.
     * </p>
     * <p>
     * If the provided {@code resourceName} is not absolute, i. e. starts with a "/", it will be
     * sanitized by prepending such a slash.
     * </p>
     *
     * @return A new {@link InputStream} instance.
     */
    public static InputStream provideStream(Class<?> clientClass, String resourceName) {
        Objects.requireNonNull(clientClass, "clientClass");
        Objects.requireNonNull(resourceName, "resourceName");

        String absoluteFilename = makeAbsolute(resourceName);
        InputStream result = clientClass.getResourceAsStream(absoluteFilename);
        assertNotNull("Could not find resource on classpath: " + resourceName, result);
        return result;
    }

    /**
     * <p>
     * Read all bytes from {@code resourceName} using {@code clientClass's} class loader.
     * </p>
     * <p>
     * If the provided {@code resourceName} is not absolute, i. e. starts with a "/", it will be
     * sanitized by prepending such a slash.
     * </p>
     *
     * @return All bytes from the specified resource.
     */
    public static byte[] readBytes(Class<?> clientClass, String resourceName) {
        var result = new byte[0];
        try (InputStream resourceStream = provideStream(clientClass, resourceName)) {
            result = resourceStream.readAllBytes();
        } catch (IOException e) {
            fail("Could not read from resource.", e);
        }

        return result;
    }

    /**
     * <p>
     * Delete a file or directory. If the provided {@code path} points to a non empty directory, it
     * will be deleted recursively. Makes use of the Java 7 NIO API instead of the deprecated
     * {@link File} API.
     * </p>
     * <p>
     * Ignores non existing {@code path}s.
     * </p>
     * 
     * @param path - Delete the file this path points to.
     * @throws AssertionException In case {@code path} exists and deleting it ran into a problem.
     */
    public static void deleteRecursively(Path path) {
        if (Files.exists(path)) {
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                fail("Could not delete '" + path.toString() + "'.", e);
            }
        }
    }

    private static String makeAbsolute(String filename) {
        return filename.startsWith("/") ? filename : "/" + filename;
    }
}
