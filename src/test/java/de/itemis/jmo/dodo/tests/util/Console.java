package de.itemis.jmo.dodo.tests.util;

import static de.itemis.jmo.dodo.util.Sneaky.throwThat;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.internal.matchers.Any;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.PatternSyntaxException;

/**
 * Record all System.out and System.err output and provide convenience method to perform asserts on
 * their content.
 *
 * <b>Attention</b> - Don't forget to clean things up by calling {@link Console#close()} at the end
 * of each Console usage.
 */
public final class Console implements AutoCloseable, AfterTestExecutionCallback {
    private static final PrintStream ORIGINAL_STDOUT = System.out;
    private static final PrintStream ORIGINAL_STDERR = System.err;

    private Path tmpFile;
    private final AtomicReference<PrintStream> sideChannel;
    private final PrintStream reroutedStdOutPrint;
    private final PrintStream reroutedStdErrPrint;
    private final TeeOutputStream reroutedStdOut;
    private final TeeOutputStream reroutedStdErr;


    /**
     * <p>
     * Setup this instance to collect stdout and stderr output for later analysis. stdout and stderr
     * will be rerouted when this method returns. Output will still be printed to console but is
     * also recorded.
     * </p>
     * <p>
     * Call {@link #close()} or {@link #afterTestExecution(ExtensionContext)} to free up resources
     * and undo the rerouting.
     * </p>
     */
    public Console() {
        this.tmpFile = createTmpFile();
        this.sideChannel = new AtomicReference<>(createConsoleStream(tmpFile));

        reroutedStdOut = new TeeOutputStream(ORIGINAL_STDOUT, sideChannel);
        reroutedStdErr = new TeeOutputStream(ORIGINAL_STDERR, sideChannel);
        reroutedStdOutPrint = new PrintStream(reroutedStdOut);
        reroutedStdErrPrint = new PrintStream(reroutedStdErr);

        System.setOut(reroutedStdOutPrint);
        System.setErr(reroutedStdErrPrint);

    }

    /**
     * Undo rerouting of stderr and stdout and release all resources. This instance becomes unusable
     * when this method returns.
     */
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        close();
    }

    private Path createTmpFile() {
        try {
            return Files.createTempFile("dodo", ".console");
        } catch (Exception e) {
            throw new RuntimeException("Console initalization failed with " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    private void deleteTmpFile() {
        if (tmpFile != null) {
            try {
                Files.deleteIfExists(tmpFile);
            } catch (IOException e) {
                ORIGINAL_STDERR.println("WARN - Possible resource leak: Could not delete temporary test file '" + tmpFile.toString() + "' - "
                    + e.getClass().getSimpleName() + ": " + e.getMessage());
            } finally {
                tmpFile = null;
                sideChannel.get().close();
            }
        }
    }

    /**
     * <p>
     * Register code to be run any time the provided {@code matchingLine} gets written to the
     * console. The line matching algorithm uses a "contains" logic, rather than an "equals" logic
     * to make matching a bit more fuzzy.
     * </p>
     * <p>
     * <b>Example:</b>
     *
     * <pre>
     * &#64;Test
     * public void test() {
     *     AtomicInteger counter = new AtomicInteger();
     *     this.console.registerCallback(() -> counter.incrementAndGet(), "match");
     *     System.out.println("match");
     *     System.out.println("nomatch");
     *     System.out.println("nomatch");
     *     Assert.assertEquals(3, counter.get()); // passed
     * }
     * </pre>
     * </p>
     *
     * @param callback - Run this code.
     * @param expectedLine - Match this line.
     * @throws Any exception (also checked exceptions) that may be thrown by the {@code callback}.
     */
    public void registerCallback(Callable<?> callback, String expectedLine) {
        Runnable runnable = new LineCountingCallback(callback, expectedLine, this);

        reroutedStdOut.registerCallback(runnable);
        reroutedStdErr.registerCallback(runnable);
    }

    /**
     * Undo rerouting of stderr and stdout and release all resources. This instance becomes unusable
     * when this method returns.
     */
    @Override
    public void close() {
        sideChannel.get().close();
        reroutedStdOutPrint.close();
        reroutedStdErrPrint.close();
        deleteTmpFile();
        System.setErr(ORIGINAL_STDERR);
        System.setOut(ORIGINAL_STDOUT);
        System.out.println("------------------------------------------------------------------");
    }

    /**
     * @return A {@link PrintStream} that can be used to write data directly to the console. Think
     *         of it as replacement to stdout.
     */
    public PrintStream asPrintStream() {
        return reroutedStdOutPrint;
    }

    /**
     * @return All data that has been logged to the console. Encoding is UTF-8.
     */
    public String getFullLog() {
        flush();

        String result = null;
        try {
            result = new String(readAllBytes(tmpFile), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throwThat(e);
        }

        return result;
    }

    private void flush() {
        sideChannel.get().flush();
        reroutedStdOutPrint.flush();
        reroutedStdErrPrint.flush();
    }

    /**
     * Discard all recorded log messages.
     */
    public synchronized void clear() {
        flush();
        sideChannel.get().close();
        deleteTmpFile();
        tmpFile = createTmpFile();
        sideChannel.set(createConsoleStream(tmpFile));
    }

    /**
     * <p>
     * Like {@link #anyLineMatches(String)} but does not use a regex but a pure String comparison.
     * </p>
     * The line matching algorithm uses a "contains" logic, rather than an "equals" logic to make
     * matching a bit more fuzzy.
     * </p>
     * <p>
     * <b>Example:</b>
     *
     * <pre>
     * &#64;Test
     * public void test() {
     *     System.out.println("nomatch");
     *     Assert.assertTrue(console.anyLineContains("match")); // passes
     * }
     * </pre>
     * </p>
     *
     * @param line - Match lines with this.
     * @return {@code true} if at least one line contains {@code line}. Otherwise {@code false}.
     */
    public boolean anyLineContains(String line) {
        return internalContains(line, 0) != null;
    }

    /**
     * Like {@link #anyLineContains(String)} but does also perfom the assert.
     *
     * @throws AssertionError in case no line contains {@code line}.
     */
    public void assertAnyLineContains(String line) {
        assertThat(anyLineContains(line)).as("Console did not contain '" + line + "'").isTrue();
    }

    private Integer internalContains(String line, int offset) {
        List<String> contents = new ArrayList<>();
        try {
            contents = Files.readAllLines(tmpFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        for (int i = offset; i < contents.size(); i++) {
            String content = contents.get(i);
            if (content.contains(line)) {
                return i;
            }
        }

        return null;
    }

    /**
     * Check if any line matches the provided {@code regex}.
     *
     * @param regex - Match lines with this regular expression.
     * @return {@code true} if at least one line matches {@code regex}. Otherwise {@code false}.
     * @throws PatternSyntaxException - if the regular expression's syntax is invalid.
     */
    public boolean anyLineMatches(String regex) {
        try {
            return Files.readAllLines(tmpFile).stream().anyMatch(line -> line.matches(regex));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Check if any line starts with {@code prefix}.
     *
     * @param prefix - String to match line beginnings.
     * @return {@code true} if at least one line starts with {@code prefix}. Otherwise
     *         {@code false}.
     */
    public boolean anyLineStartsWith(String prefix) {
        try {
            return Files.readAllLines(tmpFile).stream().anyMatch(line -> line.startsWith(prefix));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Check if any line ends with {@code suffix}.
     *
     * @param suffix - String to match line endings.
     * @return {@code true} if at least one line ends with {@code suffix}. Otherwise {@code false}.
     */
    public boolean anyLineEndsWith(String suffix) {
        try {
            return Files.readAllLines(tmpFile).stream().anyMatch(line -> line.endsWith(suffix));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private PrintStream createConsoleStream(Path tmpFile) {
        PrintStream err = null;
        try {
            err = new PrintStream(new FileOutputStream(tmpFile.toFile()));
        } catch (Exception e) {
            throw new RuntimeException("Could not create side channel - " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
        return err;
    }

    /**
     * Copied from <a href=
     * "https://stackoverflow.com/questions/7987395/how-to-write-data-to-two-java-io-outputstream-objects-at-once">https://stackoverflow.com/questions/7987395/how-to-write-data-to-two-java-io-outputstream-objects-at-once</a>
     */
    private final class TeeOutputStream extends OutputStream {

        private final OutputStream out;
        private final AtomicReference<PrintStream> teeSideChannel;
        private final List<Runnable> callbacks = new ArrayList<>();

        private TeeOutputStream(OutputStream out, AtomicReference<PrintStream> sideChannel) {
            this.out = out;
            this.teeSideChannel = sideChannel;
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
            teeSideChannel.get().write(b);
            runCallbacks();
        }

        @Override
        public void write(byte[] b) throws IOException {
            out.write(b);
            teeSideChannel.get().write(b);
            runCallbacks();
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            teeSideChannel.get().write(b, off, len);
            runCallbacks();
        }

        @Override
        public void flush() throws IOException {
            out.flush();
            teeSideChannel.get().flush();
        }

        @Override
        public void close() throws IOException {
            callbacks.clear();
            teeSideChannel.get().close();
        }

        private void registerCallback(Runnable callback) {
            callbacks.add(callback);
        }

        private void runCallbacks() throws IOException {
            out.flush();
            teeSideChannel.get().flush();
            callbacks.forEach(Runnable::run);
        }
    }

    private static final class LineCountingCallback implements Runnable {
        private final Callable<?> callback;
        private final String expectedLine;
        private final Console parent;

        private int lastMatchedLineNbr = 0;

        public LineCountingCallback(Callable<?> callback, String expectedLine, Console parent) {
            this.callback = callback;
            this.expectedLine = expectedLine;
            this.parent = parent;
        }

        @Override
        public void run() {
            Integer lineNbr = parent.internalContains(expectedLine, lastMatchedLineNbr);
            if (lineNbr != null) {
                try {
                    callback.call();
                } catch (Throwable t) {
                    throwThat(t);
                }

                lastMatchedLineNbr = lineNbr + 1;
            }
        }
    }
}
